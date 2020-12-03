package kalah.game.service;

import kalah.game.errorHandling.exceptions.GameNotFoundException;
import kalah.game.errorHandling.exceptions.InvalidMoveException;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.models.Player;
import kalah.game.repository.GameRepository;
import kalah.game.seeds.SeedsSower;
import kalah.game.seeds.SowingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static kalah.game.errorHandling.ApiErrorMessages.*;

@Service
public class GameService {
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
        Game game = retrieveGameById(gameId);

        game.getPits().sort(Comparator.comparing(Pit::getIndex));
        validatePlayerMove(game, pitIndex);

        SowingResult result = seedsSower.sow(game, pitIndex);

        if (!result.getLastUpdatedPit().isKalah()) {
            game.setCurrentPlayer(game.getNextPlayer());
        }

        return gameRepository.save(game);
    }

    private Game retrieveGameById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(String.format(GAME_NOT_FOUND.getMessage(), gameId),
                        GAME_NOT_FOUND.getError()));
    }

    private void validatePlayerMove(Game game, int pitIndex) {
        Pit pitToBeSowedFrom = game.getPits().get(pitIndex);

        if (moveIsNotAllowed(game.getCurrentPlayer(), pitToBeSowedFrom)) {
            String errorMessage = String.format(NOT_ASSIGNED_TO_PLAYER.getMessage(), pitIndex,
                    game.getCurrentPlayer().getName());

            throw new InvalidMoveException(errorMessage, NOT_ASSIGNED_TO_PLAYER.getError());
        }

        if (pitToBeSowedFrom.isEmpty()) {
            String errorMessage = String.format(SOWING_EMPTY_PIT.getMessage(), pitIndex, pitToBeSowedFrom.getAmountOfSeeds());

            throw new InvalidMoveException(errorMessage, SOWING_EMPTY_PIT.getError());
        }
    }

    private boolean moveIsNotAllowed(Player currentPlayer, Pit pitToBeSowedFrom) {
        return !pitToBeSowedFrom.isAssignedTo(currentPlayer);
    }
}
