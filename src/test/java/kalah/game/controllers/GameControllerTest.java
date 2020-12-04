package kalah.game.controllers;

import kalah.game.errorHandling.exceptions.InvalidMoveException;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.board.BoardSide;
import kalah.game.models.payloads.GamePayload;
import kalah.game.models.payloads.PitPayload;
import kalah.game.models.payloads.PlayerPayload;
import kalah.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    public static final String FIRST_PLAYER = "Jane";
    public static final String SECOND_PLAYER = "John";
    public static final int SEEDS_PER_PIT = 6;
    public static final String GAME_ID = UUID.randomUUID().toString();
    public static final int PIT_INDEX = 1;
    public static final int INVALID_PIT_INDEX = 15;
    @Mock
    private GameService gameService;

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController(gameService);
    }

    @Test
    void persistsNewGameOnDatabase() {
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER, SEEDS_PER_PIT);
        when(gameService.startGame(any(CreateNewGamePayload.class), anyInt())).thenReturn(game);

        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER, SECOND_PLAYER);

        controller.createNewGame(payload);

        verify(gameService).startGame(payload, SEEDS_PER_PIT);
    }

    @Test
    void returnsGamePayload() {
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER, SEEDS_PER_PIT);
        when(gameService.startGame(any(CreateNewGamePayload.class), anyInt())).thenReturn(game);

        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER, SECOND_PLAYER);

        GamePayload response = controller.createNewGame(payload);

        assertThat(response.getCurrentPlayer()).isEqualTo(FIRST_PLAYER);
        assertThat(response.getPlayers()).hasSize(2);

        PlayerPayload firstPlayer = response.getPlayers().get(0);
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH.name());

        PlayerPayload secondPlayer = response.getPlayers().get(1);
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH.name());

        assertPitsAreCorrectlySetOnSideOfBoard(response.getPits(), BoardSide.SOUTH);
        assertPitsAreCorrectlySetOnSideOfBoard(response.getPits(), BoardSide.NORTH);
    }

    @Test
    void distributesSeeds() {
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER, SEEDS_PER_PIT);
        when(gameService.makeMove(GAME_ID, PIT_INDEX)).thenReturn(game);

        controller.sow(GAME_ID, PIT_INDEX);

        verify(gameService).makeMove(GAME_ID, PIT_INDEX);
    }

    @Test
    void returnsSowingResults() {
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER, SEEDS_PER_PIT);
        when(gameService.makeMove(GAME_ID, PIT_INDEX)).thenReturn(game);

        GamePayload response = controller.sow(GAME_ID, PIT_INDEX);

        assertThat(response.getCurrentPlayer()).isEqualTo(FIRST_PLAYER);
        assertThat(response.getPlayers()).hasSize(2);

        PlayerPayload firstPlayer = response.getPlayers().get(0);
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH.name());

        PlayerPayload secondPlayer = response.getPlayers().get(1);
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH.name());

        assertPitsAreCorrectlySetOnSideOfBoard(response.getPits(), BoardSide.SOUTH);
        assertPitsAreCorrectlySetOnSideOfBoard(response.getPits(), BoardSide.NORTH);
    }

    @Test
    void throwsInvalidMoveExceptionWhenPitIndexIsInvalid() {
        assertThatThrownBy(() -> controller.sow(GAME_ID, INVALID_PIT_INDEX))
                .isExactlyInstanceOf(InvalidMoveException.class);
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void throwsInvalidMoveExceptionWhenSowingFromKalah(BoardSide boardSide) {
        assertThatThrownBy(() -> controller.sow(GAME_ID, boardSide.getKalahIndex()))
                .isExactlyInstanceOf(InvalidMoveException.class);
    }

    private void assertPitsAreCorrectlySetOnSideOfBoard(List<PitPayload> pits, BoardSide boardSide) {
        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> {
                    assertThat(pits.get(index).getTotalSeeds()).isEqualTo(SEEDS_PER_PIT);
                    assertThat(pits.get(index).getIndex()).isEqualTo(index);
                });

        assertThat(pits.get(boardSide.getKalahIndex()).getTotalSeeds()).isZero();
        assertThat(pits.get(boardSide.getKalahIndex()).getIndex()).isEqualTo(boardSide.getKalahIndex());
    }
}
