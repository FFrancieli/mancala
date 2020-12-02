package kalah.game.controllers;

import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.payloads.GamePayload;
import kalah.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public GamePayload createNewGame(@RequestBody CreateNewGamePayload createNewGamePayload) {
        Game game = gameService.startGame(createNewGamePayload, SEEDS_PER_PIT);

        return GamePayload.fromEnity(game);
    }

    @PutMapping("/{gameId}")
    public GamePayload sow(@PathVariable("gameId") String gameId, @RequestParam("pitIndex") int pitIndex) {
        Game game = gameService.makeMove(gameId, pitIndex);

        return GamePayload.fromEnity(game);
    }
}
