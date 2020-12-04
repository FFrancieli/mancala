package kalah.game.models.player;

import kalah.game.models.BoardSide;
import kalah.game.models.pit.Pit;
import kalah.game.models.pit.PitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {
    private static final int INDEX_OF_REGULAR_PIT = 11;

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

    @ParameterizedTest
    @MethodSource("pitsAssignedToPlayer")
    void pitIsAssignedToPlayerWhenIndexIsWithinPlayersBoardSide(BoardSide boardSide, Pit pit) {
        Player player = new Player(PLAYER_NAME, boardSide);

        assertThat(player.isAssignedTo(pit)).isTrue();
    }

    private static Stream<Arguments> pitsAssignedToPlayer() {
        return Stream.of(
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 0)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 1)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 2)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 3)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 4)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 5)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.KALAH, 6)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 7)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 8)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 9)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 10)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 11)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 12)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.KALAH, 13))
        );
    }

    @ParameterizedTest
    @MethodSource("pitsNotAssignedToPlayer")
    void pitIsNotAssignedToPlayerWhenIndexIsOutOfPlayersBoardSideRange(BoardSide boardSide, Pit pit) {
        Player player = new Player(PLAYER_NAME, boardSide);

        assertThat(player.isAssignedTo(pit)).isFalse();
    }

    private static Stream<Arguments> pitsNotAssignedToPlayer() {
        return Stream.of(
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 0)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 1)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 2)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 3)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 4)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 5)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.KALAH, 6)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 7)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 8)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 9)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 10)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 11)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 12)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.KALAH, 13))
        );
    }

    @Test
    void returnsFalseWhenGivenIndexRefersToCurrentPlayersKalah() {
        Player player = new Player(PLAYER_NAME, BoardSide.NORTH);

        assertThat(player.isOpponentPlayersKalah(BoardSide.NORTH.getKalahIndex())).isFalse();
    }

    @Test
    void returnsFalseWhenGivenIndexDoesNotReferToKalah() {
        Player player = new Player(PLAYER_NAME, BoardSide.NORTH);

        assertThat(player.isOpponentPlayersKalah(INDEX_OF_REGULAR_PIT)).isFalse();
    }

    @Test
    void returnsTrueWhenGivenIndexRefersToOpponentssKalah() {
        Player player = new Player(PLAYER_NAME, BoardSide.NORTH);

        assertThat(player.isOpponentPlayersKalah(BoardSide.SOUTH.getKalahIndex())).isTrue();
    }
}
