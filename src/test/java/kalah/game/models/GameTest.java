package kalah.game.models;

import assertions.custom.PitAssert;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    public static final String FIRST_PLAYER_NAME = "John";
    public static final String SECOND_PLAYER_NAME = "Jane";
    public static final int SEEDS_ON_PIT = 5;

    private static final int TOTAL_AMOUNT_OF_KALAH = 2;
    private static final int TOTAL_AMOUNT_REGULAR_PITS = 12;
    private static final int TOTAL_AMOUNT_OF_PITS = 14;

    @Test
    void initializesListOfPits() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        assertThat(game.getPits()).isNotNull()
                .isNotEmpty()
                .hasSize(TOTAL_AMOUNT_OF_PITS);
    }

    @Test
    void mustHave12PitsWithGivenNumberOfSeedsPerPit() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        List<Pit> regularPits = game.getPits()
                .stream()
                .filter(pit -> !pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(regularPits).hasSize(TOTAL_AMOUNT_REGULAR_PITS);

        regularPits.forEach(pit ->
                PitAssert.assertThat(pit).amountOfSeedsIs(SEEDS_ON_PIT));
    }

    @Test
    void mustHaveTwoKalahWithoutSeeds() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        List<Pit> kalah = game.getPits()
                .stream()
                .filter(pit -> pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(kalah).hasSize(TOTAL_AMOUNT_OF_KALAH);

        kalah.forEach(pit -> PitAssert.assertThat(pit).hasZeroSeeds());
    }

    @Test
    void createsFirstPlayerAssignedToSouthSideOfBoardOnGameInstantiation() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        Player firstPlayer = game.getFirstPlayer();

        assertThat(firstPlayer).isNotNull();
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH);
    }

    @Test
    void createsSecondPlayerAssignedToNorthSideOfBoardOnGameInstantiation() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        Player secondPlayer = game.getSecondPlayer();

        assertThat(secondPlayer).isNotNull();
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER_NAME);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH);
    }

    @Test
    void createsBoardWithCorrectAmountOfSeedsPerPitOnGameInstantiation() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        long pitsWithCorrectAmountPfSeeds = game.getPits()
                .stream()
                .filter(pit -> pit.getAmountOfSeeds() == SEEDS_ON_PIT)
                .count();


        assertThat(pitsWithCorrectAmountPfSeeds).isEqualTo(12);
    }

    @Test
    void firstPlayerStartTheGame() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        Player currentPlayer = game.getCurrentPlayer();

        assertThat(currentPlayer).isEqualTo(game.getFirstPlayer());
    }

    @Test
    void replaceCurrentUser() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        Game gameWithNewCurrentPlayer = game.replace(game.getSecondPlayer());

        assertThat(gameWithNewCurrentPlayer.getCurrentPlayer()).isEqualTo(game.getSecondPlayer());
    }
}
