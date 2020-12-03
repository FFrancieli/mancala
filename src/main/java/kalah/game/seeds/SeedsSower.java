package kalah.game.seeds;

import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.models.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeedsSower {
    public static final int MAXIMUM_PIT_INDEX = 13;

    public SowingResult sow(Game game, int pitId) {
        List<Pit> pits = List.copyOf(game.getPits());
        Pit sowingFromPit = Pit.newInstance(pits.get(pitId));

        pits.get(pitId).removeAllSeeds();

        Pit lastUpdatedPit = distributeSeeds(game, sowingFromPit, pits);

        if (canCaptureOppositePlayerSeeds(game.getCurrentPlayer(), lastUpdatedPit)) {
            captureOppositePlayerSeeds(game.getCurrentPlayer(), pits, lastUpdatedPit);
        }

        return new SowingResult(pits, lastUpdatedPit);
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
