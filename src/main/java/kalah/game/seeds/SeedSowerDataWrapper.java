package kalah.game.seeds;

import kalah.game.models.Pit;
import kalah.game.models.Player;
import lombok.Getter;

import java.util.List;

@Getter
public class SeedSowerDataWrapper {

    private final Player currentPlayer;
    private final List<Pit> pits;
    private final Pit sowingFromPit;

    public SeedSowerDataWrapper(Player currentPlayer, List<Pit> pits, Pit sowingFromPit) {
        this.currentPlayer = currentPlayer;
        this.pits = pits;
        this.sowingFromPit = Pit.newInstance(sowingFromPit);
    }

    public int sowingFromPitIndex() {
        return sowingFromPit.getIndex();
    }

    public int getAmountOfSeedsToSow() {
        return sowingFromPit.getAmountOfSeeds();
    }

    public List<Pit> getPits() {
        return pits;
    }

    public boolean isOpponentPlayersKalah(int otherKalahIndex) {
        int opponentsKalahIndex = currentPlayer.getBoardSide().getOpositeSideKalahIndex();

        return opponentsKalahIndex == otherKalahIndex;
    }

    public int getIndexOfCurrentPlayerKalah() {
        return currentPlayer.getBoardSide().getKalahIndex();
    }
}
