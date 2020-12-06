import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import kalah.game.KalahApplication;
import kalah.game.models.BoardSide;
import kalah.game.models.game.payloads.CreateNewGamePayload;
import kalah.game.models.game.payloads.GamePayload;
import kalah.game.models.pit.PitPayload;
import kalah.game.models.player.PlayerPayload;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.DEFAULT_PORT;
import static io.restassured.RestAssured.given;
import static kalah.game.models.BoardSide.NORTH;
import static kalah.game.models.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(classes = {KalahApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application_test.properties")
class GameIntegrationTest {
    private static RedisServer redisServer;

    private static final String GAME_ENDPOINT = "/api/game";
    private static final String SOW_ENDPOINT = "/api/game/{gameId}";

    private static final String INVALID_GAME_ID = "d49050e3-9bd3-4584-88da-7d78f4b2ea4c";

    private static final String FIRST_PLAYER = "Jane";
    private static final String SECOND_PLAYER = "John";

    private static final int AMOUNT_OF_SEEDS = 6;

    private static final int INVALID_PIT_INDEX = 19;
    private static final int KALAH_SOUTH_INDEX = 6;
    private static final int KALAH_NORTH_INDEX = 13;
    private static final int PIT_INDEX_ON_NORTH_OF_BOARD = 10;

    @BeforeAll
    static void beforeAll() throws IOException {
        RestAssured.baseURI = GAME_ENDPOINT;
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = DEFAULT_PORT;

        redisServer = new RedisServer();
    }

    @Test
    void shouldStartNewGame() {
        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER, SECOND_PLAYER);

        GamePayload gamePayload = createGame(payload)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value()).extract().body().as(GamePayload.class);

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

    @Test
    void returnsHTTPStatusNotFoundWhenTryingToUpdateUnexistentGame() {
        given()
                .param("pitIndex", 0)
                .when()
                .put(GAME_ENDPOINT + "/" + INVALID_GAME_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Cannot find game session with id d49050e3-9bd3-4584-88da-7d78f4b2ea4c"))
                .body("error", equalTo("NotFound"));
    }

    @Test
    void returnsHttpStatusBadRequestOnSowingFromInvalidPit() {
        given()
                .param("pitIndex", INVALID_PIT_INDEX)
                .when()
                .put(SOW_ENDPOINT, INVALID_GAME_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Pit index must be between 0 and 5 or 7 and 12. Received value: 19"))
                .body("error", equalTo("InvalidPitIndex"));
    }

    @ParameterizedTest
    @ValueSource(ints = {KALAH_SOUTH_INDEX, KALAH_NORTH_INDEX})
    void returnsHttpStatusConflictOnAttemptToSowFromKalah(int kalahIndex) {
        given()
                .param("pitIndex", kalahIndex)
                .when()
                .put(SOW_ENDPOINT, INVALID_GAME_ID)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo("Pit index must be between 0 and 5 or 7 and 12. Received value: " + kalahIndex))
                .body("error", equalTo("AttemptToSowFromKalah"));
    }

    @Test
    void returnsHttpStatusConflictOnAttemptToSowFromPitNotAssignedToPlayer() {
        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER, SECOND_PLAYER);
        String gameId = createGame(payload).then().extract().path("id");

        given()
                .param("pitIndex", PIT_INDEX_ON_NORTH_OF_BOARD)
                .when()
                .put(SOW_ENDPOINT, gameId)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo(String.format("Pit with id %s is not assigned to Jane", PIT_INDEX_ON_NORTH_OF_BOARD)))
                .body("error", equalTo("PitNotAssignedToPlayer"));
    }

    private Response createGame(CreateNewGamePayload payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(GAME_ENDPOINT);
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

