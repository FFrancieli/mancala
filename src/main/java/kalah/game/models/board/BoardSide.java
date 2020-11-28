package kalah.game.models.board;

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
}
