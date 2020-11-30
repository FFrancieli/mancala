import kalah.game.KalahApplication;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.board.BoardSide;
import kalah.game.models.payloads.GamePayload;
import kalah.game.models.payloads.PitPayload;
import kalah.game.models.payloads.PlayerPayload;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static kalah.game.models.board.BoardSide.NORTH;
import static kalah.game.models.board.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(classes = {KalahApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GameIntegrationTest {
    private static RedisServer redisServer;
    private static final String GAME_ENDPOINT = "http://localhost:8080/api/game";

    private static final String FIRST_PLAYER = "Jane";
    private static final String SECOND_PLAYER = "John";
    public static final int AMOUNT_OF_SEEDS = 6;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeAll
    static void beforeAll() throws IOException {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @Test
    void shouldStartNewGame() {
        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER, SECOND_PLAYER);

        HttpEntity<CreateNewGamePayload> request = new HttpEntity<>(payload);
        ResponseEntity<GamePayload> response = restTemplate.exchange(GAME_ENDPOINT, HttpMethod.POST, request, GamePayload.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        GamePayload gamePayload = response.getBody();

        assertThat(gamePayload.getId()).isNotNull()
                .isNotBlank();

        assertThat(gamePayload.getCurrentPlayer()).isEqualTo(FIRST_PLAYER);
        assertThat(gamePayload.getPlayers()).hasSize(2);

        PlayerPayload firstPlayer = gamePayload.getPlayers().get(0);
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH.name());

        PlayerPayload secondPlayer = gamePayload.getPlayers().get(1);
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH.name());

        List<PitPayload> pits = gamePayload.getPits();
        assertThat(pits).hasSize(14);

        assertPitsAreCorrectlySetOnSideOfBoard(pits, SOUTH);
        assertPitsAreCorrectlySetOnSideOfBoard(pits, NORTH);
    }

    private void assertPitsAreCorrectlySetOnSideOfBoard(List<PitPayload> pits, BoardSide boardSide) {
        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index).getTotalSeeds()).isEqualTo(AMOUNT_OF_SEEDS);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                });

        assertThat(pits.get(boardSide.getKalahIndex()).getTotalSeeds()).isZero();
        assertThat(pits.get(boardSide.getKalahIndex()).getIndex()).isEqualTo(boardSide.getKalahIndex());
    }

    @AfterAll
    static void afterAll() {
        redisServer.stop();
    }
}

