package kalah.game.models;

import kalah.game.models.board.Board;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    public static final String FIRST_PLAYER_NAME = "John";
    public static final String SECOND_PLAYER_NAME = "Jane";
    public static final int SEEDS_ON_PIT = 5;

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

        Board board = game.getBoard();

        assertThat(board).isNotNull();

        long pitsWithCorrectAmountPfSeeds = board.getPits()
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
}
