package kalah.game.models.payloads;

import kalah.game.models.Game;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GamePayloadTest {

    private static final int SEEDS_ON_PIT = 6;
    private static final String SECOND_PLAYER_NAME = "second_name";
    private static final String FIRST_PLAYER_NAME = "first_name";

    @Test
    void convertsCurrentPlayerName() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        GamePayload gamePayload = GamePayload.fromEnity(game);

        assertThat(gamePayload.getCurrentPlayer()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(gamePayload.getPlayers()).hasSize(2);

        PlayerPayload firstPlayer = gamePayload.getPlayers().get(0);
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH.name());

        PlayerPayload secondPlayer = gamePayload.getPlayers().get(1);
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER_NAME);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH.name());

        assertPitsAreCorrectlySetOnSideOfBoard(gamePayload.getPits(), BoardSide.SOUTH);
        assertPitsAreCorrectlySetOnSideOfBoard(gamePayload.getPits(), BoardSide.NORTH);
    }

    private void assertPitsAreCorrectlySetOnSideOfBoard(List<PitPayload> pits, BoardSide boardSide) {
        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index).getTotalSeeds()).isEqualTo(SEEDS_ON_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                });

        assertThat(pits.get(boardSide.getKalahIndex()).getTotalSeeds()).isZero();
        assertThat(pits.get(boardSide.getKalahIndex()).getIndex()).isEqualTo(boardSide.getKalahIndex());
    }
}
