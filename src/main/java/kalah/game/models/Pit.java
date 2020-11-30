package kalah.game.models;

import static com.google.common.base.Preconditions.checkArgument;

public class Pit {
    private static final int EMPTY_PIT = 0;
    private final PitType pitType;
    private final int index;
    private int amountOfSeeds;

    public Pit(PitType pitType, int index) {
        this.pitType = pitType;
        this.index = index;
        this.amountOfSeeds = EMPTY_PIT;
    }

    public Pit(PitType pitType, int index, int amountOfSeeds) {
        checkArgument(kalahHasValidAmountOfSeeds(pitType, amountOfSeeds),
                invalidInitialAmountOfSeedsOnKalahMessageWrapper(amountOfSeeds));

        this.pitType = pitType;
        this.index = index;
        this.amountOfSeeds = amountOfSeeds;
    }

    private String invalidInitialAmountOfSeedsOnKalahMessageWrapper(int amountOfSeeds) {
        return String.format("Kalah must be initialized with zero seeds. Received %d seeds on constructor", amountOfSeeds);
    }

    private boolean kalahHasValidAmountOfSeeds(PitType pitType, int amountOfSeeds) {
        return !pitType.isKalah() || amountOfSeeds == 0;
    }

    public int getAmountOfSeeds() {
        return amountOfSeeds;
    }

    public PitType getPitType() {
        return pitType;
    }

    public int getIndex() {
        return index;
    }
}
