package kalah.game.service;

import kalah.game.errorHandling.GameNotFoundException;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.repository.GameRepository;
import kalah.game.seeds.SeedsSower;
import kalah.game.seeds.SowingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final String GAME_NOT_FOUND_ERROR = "Cannot find game session with id %s";

    private final GameRepository gameRepository;
    private final SeedsSower seedsSower;

    @Autowired
    public GameService(GameRepository gameRepository, SeedsSower seedsSower) {
        this.gameRepository = gameRepository;
        this.seedsSower = seedsSower;
    }

    public Game startGame(CreateNewGamePayload payload, int seedsPerPit) {
        Game game = new Game(payload.getFirstPlayerName(), payload.getSecondPlayerName(), seedsPerPit);

        return gameRepository.save(game);
    }

    public Game makeMove(String gameId, int pitIndex) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(String.format(GAME_NOT_FOUND_ERROR, gameId)));

        SowingResult result = seedsSower.sow(game, pitIndex);

        if (!result.getLastUpdatedPit().isKalah()) {
            game.setCurrentPlayer(game.getNextPlayer());
        }

        return gameRepository.save(game);
    }
}
