package kalah.game.models.board;

import kalah.game.models.Pit;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static assertions.custom.PitAssert.assertThat;
import static kalah.game.models.board.BoardSide.NORTH;
import static kalah.game.models.board.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;

class PitsInitializerTest {

    private static final int NUMBER_OF_SEEDS_PER_PIT = 6;

    @Test
    void hasSixPitsForSouthSideOfTheBoard() {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(pits).hasSize(14);

        IntStream.range(SOUTH.getFirstPitIndex(), SOUTH.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index))
                            .isRegularPit()
                            .amountOfSeedsIs(NUMBER_OF_SEEDS_PER_PIT)
                            .indexIs(index);
                });
    }

    @Test
    void hasOneKalahOnSouthSideOfTheBoard() {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = pits.get(SOUTH.getKalahIndex());

        assertThat(kalah)
                .hasZeroSeeds()
                .indexIs(SOUTH.getKalahIndex())
                .isKalah();
    }

    @Test
    void hasSixPitsForNorthSideOfTheBoard() {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(pits).hasSize(14);

        IntStream.range(NORTH.getFirstPitIndex(), NORTH.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index))
                            .isRegularPit()
                            .amountOfSeedsIs(NUMBER_OF_SEEDS_PER_PIT)
                            .indexIs(index);
                });
    }

    @Test
    void hasOneKalahOnNorthSideOfTheBoard() {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = pits.get(NORTH.getKalahIndex());

        assertThat(kalah)
                .hasZeroSeeds()
                .indexIs(NORTH.getKalahIndex())
                .isKalah();
    }
}
