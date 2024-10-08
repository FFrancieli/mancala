package kalah.game.models.pit;

import kalah.game.models.player.Player;
import kalah.game.models.BoardSide;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

@NoArgsConstructor
public class Pit implements Serializable {
    private static final int EMPTY_PIT = 0;
    private PitType pitType;
    private int index;
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

    public static Pit newInstance(Pit pit) {
        return new Pit(pit.pitType, pit.index, pit.amountOfSeeds);
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

    public void sow() {
        this.amountOfSeeds += 1;
    }

    public void removeAllSeeds() {
        this.amountOfSeeds = 0;
    }

    public void setAmountOfSeeds(int amountOfSeeds) {
        this.amountOfSeeds = amountOfSeeds;
    }

    public int findOppositePitIndex() {
        if (this.pitType.isKalah()) {
            return BoardSide.getSideByKalahIndex(this.index).getOpositeSideKalahIndex();
        }

        return this.index + (2 * calculateDistanceToKalahOnSouth());
    }

    private int calculateDistanceToKalahOnSouth() {
        return BoardSide.SOUTH.getKalahIndex() - this.index;
    }

    public void addSeeds(int extraSeeds) {
        this.amountOfSeeds += extraSeeds;
    }

    public boolean hasSeeds() {
        return amountOfSeeds > 0;
    }

    public boolean isEmpty() {
        return amountOfSeeds == 0;
    }

    public boolean isAssignedTo(Player player) {
        BoardSide playerBoardSide = player.getBoardSide();

        return index >= playerBoardSide.getFirstPitIndex() && playerBoardSide.getKalahIndex() >= index;
    }

    public boolean isKalah() {
        return this.pitType.isKalah();
    }
}
