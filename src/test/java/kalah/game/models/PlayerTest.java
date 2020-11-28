package kalah.game.models;

import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    private static final String PLAYER_NAME = "player";
    private static final String ANOTHER_PLAYER_NAME = "anotherPlayer";
    private static final Player PLAYER = new Player(PLAYER_NAME, BoardSide.SOUTH);

    @Test
    void playersAreEqualWhenBothHaveSameNameAndBoardSide() {
        Player anotherPlayer = new Player(PLAYER_NAME, BoardSide.SOUTH);

        assertThat(anotherPlayer).isEqualTo(PLAYER);
    }

    @Test
    void playersAreNotEqualWhenNamesAreDifferent() {
        Player anotherPlayer = new Player(ANOTHER_PLAYER_NAME, BoardSide.SOUTH);

        assertThat(anotherPlayer).isNotEqualTo(PLAYER);
    }

    @Test
    void playersAreNotEqualWhenBoardSidesAreDifferent() {
        Player anotherPlayer = new Player(PLAYER_NAME, BoardSide.NORTH);

        assertThat(anotherPlayer).isNotEqualTo(PLAYER);
    }

    @Test
    void playersAreNotEqualWhenBoardSideAndNameAreDifferent() {
        Player anotherPlayer = new Player(ANOTHER_PLAYER_NAME, BoardSide.NORTH);

        assertThat(anotherPlayer).isNotEqualTo(PLAYER);
    }

    @Test
    void nullPlayerIsNotEqualToPlayer() {
        Player anotherPlayer = null;

        assertThat(anotherPlayer).isNotEqualTo(PLAYER);
    }

    @Test
    void objectWithDifferentTypeIsNotEqualToPlayer() {
        assertThat(new StringBuffer()).isNotEqualTo(PLAYER);
    }

    @Test
    void equalPlayersHaveTheSameHashCode() {
        Player anotherPlayer = new Player(PLAYER_NAME, BoardSide.SOUTH);

        assertThat(anotherPlayer).hasSameHashCodeAs(PLAYER);
    }

    @Test
    void differentPlayersPlayersHaveDifferentHashCode() {
        Player anotherPlayer = new Player(ANOTHER_PLAYER_NAME, BoardSide.SOUTH);

        assertThat(anotherPlayer.hashCode()).isNotEqualTo(PLAYER.hashCode());
    }
}
