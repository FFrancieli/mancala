package kalah.game.models.board;

import kalah.game.models.Pit;
import kalah.game.models.PitType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static kalah.game.models.board.BoardSide.NORTH;
import static kalah.game.models.board.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;

class BoarPitsInitializerTest {

    private static final int NUMBER_OF_SEEDS_PER_PIT = 6;

    @Test
    void hasSixPitsForSouthSideOfTheBoard() {
        List<Pit> pits = BoarPitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(pits).hasSize(14);

        IntStream.range(SOUTH.getFirstPitIndex(), SOUTH.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index).getAmountOfSeeds()).isEqualTo(NUMBER_OF_SEEDS_PER_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                    assertThat(pits.get(index).getPitType()).isEqualTo(PitType.REGULAR);
                });
    }

    @Test
    void hasOneKalahOnSouthSideOfTheBoard() {
        List<Pit> pits = BoarPitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = pits.get(SOUTH.getKalahIndex());

        assertThat(kalah.getAmountOfSeeds()).isZero();
        assertThat(kalah.getIndex()).isEqualTo(SOUTH.getKalahIndex());
        assertThat(kalah.getPitType()).isEqualTo(PitType.KALAH);
    }

    @Test
    void hasSixPitsForNorthSideOfTheBoard() {
        List<Pit> pits = BoarPitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(pits).hasSize(14);

        IntStream.range(NORTH.getFirstPitIndex(), NORTH.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index).getAmountOfSeeds()).isEqualTo(NUMBER_OF_SEEDS_PER_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                    assertThat(pits.get(index).getPitType()).isEqualTo(PitType.REGULAR);
                });
    }

    @Test
    void hasOneKalahOnNorthSideOfTheBoard() {
        List<Pit> pits = BoarPitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = pits.get(NORTH.getKalahIndex());
        assertThat(kalah.getAmountOfSeeds()).isZero();
        assertThat(kalah.getIndex()).isEqualTo(NORTH.getKalahIndex());
        assertThat(kalah.getPitType()).isEqualTo(PitType.KALAH);
    }
}
