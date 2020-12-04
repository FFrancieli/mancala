package kalah.game.service;

import kalah.game.errorHandling.exceptions.GameNotFoundException;
import kalah.game.errorHandling.exceptions.InvalidMoveException;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.repository.GameRepository;
import kalah.game.state.GameState;
import kalah.game.state.action.GameAction;
import kalah.game.state.action.SowSeedsAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static kalah.game.models.board.BoardSide.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private static final int PIT_INDEX = 1;
    private static final int SEEDS_PER_PIT = 6;
    private static final int PIT_INDEX_NORTH = 8;

    private static final String FIRST_PLAYER_NAME = "firstPlayer";
    private static final String SECOND_PLAYER_NAME = "secondPlayer";
    private static final String GAME_ID = UUID.randomUUID().toString();

    private Game game;

    @Captor
    ArgumentCaptor<Game> gameArgumentCaptor;

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(gameRepository);

        game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_PER_PIT);
    }

    @Test
    void persistsNewGame() {
        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME);

        gameService.startGame(payload, SEEDS_PER_PIT);

        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game gameToBePersisted = gameArgumentCaptor.getValue();

        assertThat(gameToBePersisted.getPlayers().get(0).getName()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(gameToBePersisted.getPlayers().get(1).getName()).isEqualTo(SECOND_PLAYER_NAME);

        Pit firstPitInTheBoard = gameToBePersisted.getPits().get(0);
        assertThat(firstPitInTheBoard.getAmountOfSeeds()).isEqualTo(SEEDS_PER_PIT);
    }

    @Test
    void returnsCreatedGame() {
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME);

        Game createdGame = gameService.startGame(payload, SEEDS_PER_PIT);

        assertThat(createdGame).isEqualTo(game);
    }

    @Test
    void retrievesGameFromStorageBeforeMakingMove() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void throwsGameNotFoundExceptionWhenNoGameWithGivenIdIsFound() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.makeMove(GAME_ID, PIT_INDEX))
                .hasMessage(String.format("Cannot find game session with id %s", GAME_ID))
                .isExactlyInstanceOf(GameNotFoundException.class);
    }

    @Test
    void performsGameActionOnMakeMove() {
        GameState mockedState = mock(GameState.class);
        game.setGameState(mockedState);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(mockedState).execute(any(Game.class), any(GameAction.class));
    }

    @Test
    void performsGameActionAgainstClonedInstanceOfGame() {
        GameState mockedState = mock(GameState.class);
        game.setGameState(mockedState);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(mockedState).execute(gameArgumentCaptor.capture(), any(GameAction.class));

        Game clonedGame = gameArgumentCaptor.getValue();

        assertThat(clonedGame.getId()).isEqualTo(game.getId());
        assertThat(clonedGame.getCurrentPlayer()).isEqualTo(game.getCurrentPlayer());
        assertThat(clonedGame.getPlayers()).isEqualTo(game.getPlayers());
        assertThat(clonedGame.getPits()).isEqualTo(game.getPits());
        assertThat(clonedGame.getStatus()).isEqualTo(game.getStatus());
        assertThat(clonedGame.getWinner()).isEqualTo(game.getWinner());
        assertThat(clonedGame.getGameState()).isEqualTo(game.getGameState());
    }

    @Test
    void performsActionAgainstCorrectPitIndexWhenPerformingMove() {
        GameState mockedState = mock(GameState.class);
        game.setGameState(mockedState);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        ArgumentCaptor<SowSeedsAction> gameActionArgumentCaptor = ArgumentCaptor.forClass(SowSeedsAction.class);
        verify(mockedState).execute(any(Game.class), gameActionArgumentCaptor.capture());

        SowSeedsAction sowSeedsAction = gameActionArgumentCaptor.getValue();
        assertThat(sowSeedsAction.getPitIndex()).isEqualTo(PIT_INDEX);
    }

    @Test
    void updateGameWhenPerformingPlayerMove() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void returnsUpdatedGame() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game updatedGame = gameService.makeMove(GAME_ID, PIT_INDEX);

        assertThat(updatedGame).isEqualTo(game);
    }

    @Test
    void throwsInvalidMoveExceptionWhenCurrentPlayerIsNotAssignedToPit() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_PER_PIT);
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameService.makeMove(GAME_ID, PIT_INDEX_NORTH))
                .isExactlyInstanceOf(InvalidMoveException.class)
                .hasMessage("Pit with id 8 is not assigned to firstPlayer");
    }

    @Test
    void throwsInvalidMoveExceptionWhenTargetPitIsEmpty() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_PER_PIT);
        game.getPits().get(0).removeAllSeeds();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameService.makeMove(GAME_ID, SOUTH.getFirstPitIndex()))
                .isExactlyInstanceOf(InvalidMoveException.class)
                .hasMessage("Cannot sow from empty pit. Pit on index 0 has 0 seeds");
    }
}
