package kalah.game.state.action;

import kalah.game.models.game.Game;
import kalah.game.models.pit.Pit;
import kalah.game.models.player.Player;
import lombok.Getter;

import java.util.List;

@Getter
public class SowSeedsAction implements GameAction {
    public static final int MAXIMUM_PIT_INDEX = 13;

    public SowSeedsAction(Integer pitId) {
        this.pitIndex = pitId;
    }

    private final Integer pitIndex;

    public GameActionResult performAction(Game game) {
        List<Pit> pits = List.copyOf(game.getPits());
        Pit sowingFromPit = Pit.newInstance(pits.get(pitIndex));

        pits.get(pitIndex).removeAllSeeds();

        Pit lastUpdatedPit = distributeSeeds(game, sowingFromPit, pits);

        if (canCaptureOppositePlayerSeeds(game.getCurrentPlayer(), lastUpdatedPit)) {
            captureOppositePlayerSeeds(game.getCurrentPlayer(), pits, lastUpdatedPit);
        }

        return new GameActionResult(game, lastUpdatedPit);
    }

    private Pit distributeSeeds(Game game, Pit sowingFromPit, List<Pit> pits) {
        int amountOfSeeds = sowingFromPit.getAmountOfSeeds();
        int currentPitIndex = sowingFromPit.getIndex() + 1;

        Pit lastUpdatedPit = sowingFromPit;
        while (amountOfSeeds > 0) {
            if (!game.isCurrentPlayerOpponentsKalah(currentPitIndex)) {
                lastUpdatedPit = pits.get(currentPitIndex);
                lastUpdatedPit.sow();
                amountOfSeeds -= 1;
            }

            currentPitIndex = calculateNextIndex(currentPitIndex);
        }

        return lastUpdatedPit;
    }

    private void captureOppositePlayerSeeds(Player currentPlayer, List<Pit> pits, Pit lastUpdatedPit) {
        Pit oppositePit = pits.get(lastUpdatedPit.findOppositePitIndex());

        if (oppositePit.hasSeeds()) {
            int newAmountOfSeeds = lastUpdatedPit.getAmountOfSeeds() + oppositePit.getAmountOfSeeds();

            pits.get(currentPlayer.getBoardSide().getKalahIndex())
                    .addSeeds(newAmountOfSeeds);

            lastUpdatedPit.removeAllSeeds();
            oppositePit.removeAllSeeds();
        }
    }

    private boolean canCaptureOppositePlayerSeeds(Player currentPlayer, Pit lastUpdatedPit) {
        return lastUpdatedPit.isAssignedTo(currentPlayer) && lastSeedLandedOnEmptyPit(lastUpdatedPit);
    }

    private boolean lastSeedLandedOnEmptyPit(Pit lastUpdatedPit) {
        return lastUpdatedPit.getAmountOfSeeds() == 1;
    }

    private int calculateNextIndex(Integer currentPitIndex) {
        if (currentPitIndex == MAXIMUM_PIT_INDEX) {
            return 0;
        }

        return (currentPitIndex % MAXIMUM_PIT_INDEX) + 1;
    }
}
