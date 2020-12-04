package kalah.game.models.pit;

public enum PitType {
    REGULAR, KALAH;

    public boolean isKalah() {
        return KALAH.equals(this);
    }
}
