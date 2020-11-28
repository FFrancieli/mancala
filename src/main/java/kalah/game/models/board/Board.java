package kalah.game.models.board;

import kalah.game.models.Pit;

import java.util.List;

public class Board {
    private final List<Pit> pits;

    public Board(int numberOfSeedsPerPit) {
        pits = BoarPitsInitializer.initializePits(numberOfSeedsPerPit);
    }

    public List<Pit> getPits() {
        return pits;
    }
}
