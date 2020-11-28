package kalah.game.models;

public enum PitType {
    REGULAR, KALAH;

    public boolean isKalah() {
        return KALAH.equals(this);
    }
}
