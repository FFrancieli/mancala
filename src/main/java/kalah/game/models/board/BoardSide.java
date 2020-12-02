package kalah.game.models.board;

import java.util.EnumSet;

public enum BoardSide {
    SOUTH(0, 6),
    NORTH(7, 13);

    private final int firstPitIndex;
    private final int kalahIndex;

    BoardSide(int firstPitIndex, int kalahIndex) {
        this.firstPitIndex = firstPitIndex;
        this.kalahIndex = kalahIndex;
    }

    public int getFirstPitIndex() {
        return firstPitIndex;
    }

    public int getKalahIndex() {
        return kalahIndex;
    }

    public int getOpositeSideKalahIndex() {
        return this.equals(NORTH) ? SOUTH.kalahIndex : NORTH.kalahIndex;
    }

    public static BoardSide getSideByKalahIndex(int indexOfKalah) {
        return EnumSet.allOf(BoardSide.class).stream()
                .filter(side -> side.kalahIndex == indexOfKalah)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("There is no Kalah on board with index %d", indexOfKalah)));
    }
}
