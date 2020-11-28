package kalah.game.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static kalah.game.models.BoardSide.NORTH;
import static kalah.game.models.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {
    private static final int NUMBER_OF_SEEDS_PER_PIT = 6;

    @Test
    void initializesListOfPitsBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        assertThat(board.getPits()).isNotNull()
                .isNotEmpty()
                .hasSize(14);
    }

    @Test
    void hasSixPitsForSouthSideOfTheBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(board.getPits()).hasSize(14);

        List<Pit> pits = board.getPits();

        IntStream.range(0, 6)
                .forEach(index -> {
                    assertThat(pits.get(index).getAmountOfSeeds()).isEqualTo(NUMBER_OF_SEEDS_PER_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                    assertThat(pits.get(index).getPitType()).isEqualTo(PitType.REGULAR);
                });
    }

    @Test
    void hasOneKalahOnSouthSideOfTheBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = board.getPits().get(6);
        assertThat(kalah.getAmountOfSeeds()).isZero();
        assertThat(kalah.getIndex()).isEqualTo(SOUTH.getKalahIndex());
        assertThat(kalah.getPitType()).isEqualTo(PitType.KALAH);
    }

    @Test
    void hasSixPitsForNorthSideOfTheBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);
        assertThat(board.getPits()).hasSize(14);

        List<Pit> pits = board.getPits();

        IntStream.range(7, 13)
                .forEach(index -> {
                    assertThat(pits.get(index).getAmountOfSeeds()).isEqualTo(NUMBER_OF_SEEDS_PER_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                    assertThat(pits.get(index).getPitType()).isEqualTo(PitType.REGULAR);
                });
    }

    @Test
    void hasOneKalahOnNorthSideOfTheBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        Pit kalah = board.getPits().get(13);
        assertThat(kalah.getAmountOfSeeds()).isZero();
        assertThat(kalah.getIndex()).isEqualTo(NORTH.getKalahIndex());
        assertThat(kalah.getPitType()).isEqualTo(PitType.KALAH);
    }
}
