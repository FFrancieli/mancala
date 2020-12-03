package kalah.game.service;

import kalah.game.errorHandling.exceptions.GameNotFoundException;
import kalah.game.errorHandling.exceptions.InvalidMoveException;
import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.models.PitType;
import kalah.game.models.board.BoardSide;
import kalah.game.repository.GameRepository;
import kalah.game.seeds.SeedsSower;
import kalah.game.seeds.SowingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private SeedsSower seedsSower;


    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(gameRepository, seedsSower);
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
        when(seedsSower.sow(game, PIT_INDEX)).thenCallRealMethod();
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
    void sowsSeedsFromGivenPit() {
        when(seedsSower.sow(game, PIT_INDEX)).thenCallRealMethod();
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(seedsSower).sow(game, PIT_INDEX);
    }

    @Test
    void updateGameAfterSowingSeeds() {
        when(seedsSower.sow(game, PIT_INDEX)).thenCallRealMethod();
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.makeMove(GAME_ID, PIT_INDEX);

        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void currentPlayerRemainsTheSameWhenLastMoveLandedOnTheirOwnKalah() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
        SowingResult result = new SowingResult(emptyList(), new Pit(PitType.KALAH, 6));
        when(seedsSower.sow(game, BoardSide.SOUTH.getFirstPitIndex())).thenReturn(result);

        gameService.makeMove(GAME_ID, BoardSide.SOUTH.getFirstPitIndex());

        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game persistedGame = gameArgumentCaptor.getValue();

        assertThat(persistedGame.getCurrentPlayer()).isEqualTo(game.getCurrentPlayer());
    }

    @Test
    void currentPlayerChangesWhenLastSowedSeedsLandsOnRegularPit() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_PER_PIT);
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        SowingResult result = new SowingResult(emptyList(), new Pit(PitType.REGULAR, 7, 7));
        when(seedsSower.sow(game, BoardSide.SOUTH.getFirstPitIndex())).thenReturn(result);

        gameService.makeMove(GAME_ID, BoardSide.SOUTH.getFirstPitIndex());

        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game persistedGame = gameArgumentCaptor.getValue();

        assertThat(persistedGame.getCurrentPlayer()).isEqualTo(game.getPlayers().get(1));
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

        assertThatThrownBy(() -> gameService.makeMove(GAME_ID, PIT_INDEX_NORTH))
                .isExactlyInstanceOf(InvalidMoveException.class)
                .hasMessage("Pit with id 8 is not assigned to firstPlayer");
    }
}
