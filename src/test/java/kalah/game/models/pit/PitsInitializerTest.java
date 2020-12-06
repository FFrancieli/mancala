package kalah.game.models.pit;

import kalah.game.models.BoardSide;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.IntStream;

import static assertions.custom.PitAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

class PitsInitializerTest {

    private static final int NUMBER_OF_SEEDS_PER_PIT = 6;

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void hasSixPitsOnEachSideOfTheBoard(BoardSide boardSide) {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(pits).hasSize(14);

        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index))
                            .isRegularPit()
                            .amountOfSeedsIs(NUMBER_OF_SEEDS_PER_PIT)
                            .indexIs(index);
                });
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void hasOneKalahAssignedToEachSideOfTheBoard(BoardSide boardSide) {
        List<Pit> pits = PitsInitializer.initializePits(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = pits.get(boardSide.getKalahIndex());

        assertThat(kalah)
                .hasZeroSeeds()
                .indexIs(boardSide.getKalahIndex())
                .isKalah();
    }
}
