package kalah.game.models.payloads;

import kalah.game.models.Player;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerPayloadTest {

    public static final String PLAYER_NAME = "Player";

    @Test
    void convertsEntityIntoPayload() {
        Player enity = new Player(PLAYER_NAME, BoardSide.SOUTH);

        PlayerPayload payload = PlayerPayload.fromEntity(enity);

        assertThat(payload).isNotNull();
        assertThat(payload.getName()).isEqualTo(PLAYER_NAME);
        assertThat(payload.getBoardSide()).isEqualTo(BoardSide.SOUTH.name());
    }
}
