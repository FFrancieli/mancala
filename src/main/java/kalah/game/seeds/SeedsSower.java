package kalah.game.seeds;

import kalah.game.models.Pit;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SeedsSower {
    public static final int MAXIMUM_PIT_INDEX = 13;

    public SowingResult sow(SeedSowerDataWrapper seedSowerData) {
        seedSowerData.getPits().sort(Comparator.comparing(Pit::getIndex));

        List<Pit> pits = seedSowerData.getPits();
        pits.get(seedSowerData.sowingFromPitIndex()).removeAllSeeds();

        Pit lastUpdatedPit = distributeSeeds(seedSowerData, pits);

        if (canCaptureOppositePlayerSeeds(seedSowerData, lastUpdatedPit)) {
            captureOppositePlayerSeeds(seedSowerData, pits, lastUpdatedPit);
        }

        return new SowingResult(pits, lastUpdatedPit);
    }

    private Pit distributeSeeds(SeedSowerDataWrapper seedSowerData, List<Pit> pits) {
        int amountOfSeeds = seedSowerData.getAmountOfSeedsToSow();
        int currentPitIndex = seedSowerData.sowingFromPitIndex() + 1;

        Pit lastUpdatedPit = seedSowerData.getSowingFromPit();
        while (amountOfSeeds > 0) {
            if (!seedSowerData.isOpponentPlayersKalah(currentPitIndex)) {
                lastUpdatedPit = pits.get(currentPitIndex);
                lastUpdatedPit.sow();
                amountOfSeeds -= 1;
            }

            currentPitIndex = calculateNextIndex(currentPitIndex);
        }

        return lastUpdatedPit;
    }

    private void captureOppositePlayerSeeds(SeedSowerDataWrapper seedSowerData, List<Pit> pits, Pit lastUpdatedPit) {
        Pit oppositePit = pits.get(lastUpdatedPit.findOppositePitIndex());

        if (oppositePit.hasSeeds()) {
            int newAmountOfSeeds = lastUpdatedPit.getAmountOfSeeds() + oppositePit.getAmountOfSeeds();

            pits.get(seedSowerData.getIndexOfCurrentPlayerKalah())
                    .addSeeds(newAmountOfSeeds);

            lastUpdatedPit.removeAllSeeds();
            oppositePit.removeAllSeeds();
        }
    }

    private boolean canCaptureOppositePlayerSeeds(SeedSowerDataWrapper seedSowerData, Pit lastUpdatedPit) {
        return isPitAssignedToPlayer(seedSowerData, lastUpdatedPit) && endedOnEmptyPit(lastUpdatedPit);
    }

    private boolean isPitAssignedToPlayer(SeedSowerDataWrapper seedSowerData, Pit lastUpdatedPit) {
        return seedSowerData.getCurrentPlayer().isAssignedTo(lastUpdatedPit);
    }

    private boolean endedOnEmptyPit(Pit lastUpdatedPit) {
        return lastUpdatedPit.getAmountOfSeeds() == 1;
    }

    private int calculateNextIndex(Integer currentPitIndex) {
        if (currentPitIndex == MAXIMUM_PIT_INDEX) {
            return 0;
        }

        return (currentPitIndex % MAXIMUM_PIT_INDEX) + 1;
    }
}
