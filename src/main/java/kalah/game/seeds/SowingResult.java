package kalah.game.seeds;

import kalah.game.models.Pit;
import lombok.Getter;

import java.util.List;

@Getter
public class SowingResult {
    private final List<Pit> updatedPits;
    private final Pit lastUpdatedPit;

    public SowingResult(List<Pit> updatedPits, Pit lastUpdatedPit) {
        this.updatedPits = updatedPits;
        this.lastUpdatedPit = lastUpdatedPit;
    }
}
