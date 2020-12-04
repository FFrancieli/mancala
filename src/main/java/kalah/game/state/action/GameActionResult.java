package kalah.game.state.action;

import kalah.game.models.Game;
import kalah.game.models.Pit;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Optional;

@Getter
@EqualsAndHashCode
public class GameActionResult {
    private final Game game;
    private final Pit lastUpdatedPit;

    public GameActionResult(Game game, Pit lastUpdatedPit) {
        this.game = game;
        this.lastUpdatedPit = lastUpdatedPit;
    }

    public GameActionResult(Game game) {
        this.game = game;
        this.lastUpdatedPit = null;
    }

    public Optional<Pit> getLastUpdatedPit() {
        return Optional.ofNullable(lastUpdatedPit);
    }
}
