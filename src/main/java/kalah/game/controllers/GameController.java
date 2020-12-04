package kalah.game.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kalah.game.errorHandling.ApiError;
import kalah.game.errorHandling.exceptions.InvalidMoveException;
import kalah.game.models.BoardSide;
import kalah.game.models.game.Game;
import kalah.game.models.game.payloads.CreateNewGamePayload;
import kalah.game.models.game.payloads.GamePayload;
import kalah.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kalah.game.errorHandling.ApiErrorMessages.INVALID_PIT_INDEX;
import static kalah.game.errorHandling.ApiErrorMessages.SOWING_FROM_KALAH;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private static final int SEEDS_PER_PIT = 6;

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public GamePayload createNewGame(@Valid @RequestBody CreateNewGamePayload createNewGamePayload) {
        Game game = gameService.startGame(createNewGamePayload, SEEDS_PER_PIT);

        return GamePayload.fromEnity(game);
    }

    @PutMapping("/{gameId}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Player move performed successfully"),
            @ApiResponse(code = 400, message = "Pit index is invalid", response = ApiError.class),
            @ApiResponse(code = 400, message = "Game with given gameId was not found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Received request is correct but move conflicts with game rules",
                    response = ApiError.class)
    })
    public GamePayload sow(@PathVariable("gameId") String gameId, @RequestParam("pitIndex") int pitIndex) {
        validatePitIndex(pitIndex);

        Game game = gameService.makeMove(gameId, pitIndex);

        return GamePayload.fromEnity(game);
    }

    private void validatePitIndex(int pitIndex) {
        if (pitIndex > 13) {
            throw new InvalidMoveException(String.format(INVALID_PIT_INDEX.getMessage(), pitIndex), INVALID_PIT_INDEX.getError());
        }

        if (pitIndex == BoardSide.NORTH.getKalahIndex() || pitIndex == BoardSide.SOUTH.getKalahIndex()) {
            throw new InvalidMoveException(String.format(SOWING_FROM_KALAH.getMessage(), pitIndex),
                    SOWING_FROM_KALAH.getError());
        }
    }
}
