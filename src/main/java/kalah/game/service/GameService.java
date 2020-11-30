package kalah.game.service;

import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startGame(CreateNewGamePayload payload, int seedsPerPit) {
        Game game = new Game(payload.getFirstPlayerName(), payload.getSecondPlayerName(), seedsPerPit);

        return gameRepository.save(game);
    }
}
