package kalah.game.models.board;

import kalah.game.models.Pit;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static assertions.custom.PitAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;


class BoardTest {
    private static final int NUMBER_OF_SEEDS_PER_PIT = 6;
    private static final int TOTAL_AMOUNT_OF_KALAH = 2;
    private static final int TOTAL_AMOUNT_REGULAR_PITS = 12;
    private static final int TOTAL_AMOUNT_OF_PITS = 14;

    @Test
    void initializesListOfPitsBoard() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        assertThat(board.getPits()).isNotNull()
                .isNotEmpty()
                .hasSize(TOTAL_AMOUNT_OF_PITS);
    }

    @Test
    void mustHave12PitsWithGivenNumberOfSeedsPerPit() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        List<Pit> regularPits = board.getPits()
                .stream()
                .filter(pit -> !pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(regularPits).hasSize(TOTAL_AMOUNT_REGULAR_PITS);

        regularPits.forEach(pit ->
                assertThat(pit).amountOfSeedsIs(NUMBER_OF_SEEDS_PER_PIT));
    }

    @Test
    void mustHaveTwoKalahWithoutSeeds() {
        Board board = new Board(NUMBER_OF_SEEDS_PER_PIT);

        List<Pit> kalah = board.getPits()
                .stream()
                .filter(pit -> pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(kalah).hasSize(TOTAL_AMOUNT_OF_KALAH);

        kalah.forEach(pit -> assertThat(pit).hasZeroSeeds());
    }
}
