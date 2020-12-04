package kalah.game.state;

import kalah.game.models.game.Game;
import kalah.game.state.action.FinishGameAction;
import kalah.game.state.action.GameAction;
import kalah.game.state.action.GameActionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OngoingGameStateTest {

    @Mock
    private GameAction gameAction;

    private Game game;

    private final OngoingGameState ongoingGameState = new OngoingGameState();

    @BeforeEach
    void setUp() {
        game = new Game("player", "another player", 5);
    }

    @Test
    void nextStateIsFinishedGameState() {
        assertThat(ongoingGameState.getNextState()).isInstanceOf(FinishedGameState.class);
    }

    @Test
    void performActionIfNoneOfTheRowsIsEmpty() {
        GameActionResult result = new GameActionResult(game);
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        ongoingGameState.execute(game, gameAction);

        verify(gameAction).performAction(game);
    }

    @Test
    void currentPlayerPlaysNextIfLastUpdatedPitIsAbsent() {
        GameActionResult result = new GameActionResult(game.newInstance());
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        Game updatedGame = ongoingGameState.execute(game, gameAction);

        assertThat(updatedGame.getCurrentPlayer()).isEqualTo(game.getCurrentPlayer());
    }

    @Test
    void currentPlayerPlaysNextIfLastUpdatedPitIsKalah() {
        GameActionResult result = new GameActionResult(game.newInstance(), game.getPits().get(13));
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        Game updatedGame = ongoingGameState.execute(game, gameAction);

        assertThat(updatedGame.getCurrentPlayer()).isEqualTo(game.getCurrentPlayer());
    }

    @Test
    void currentPlayerDoesNotPlayNextWhenLastUpdatedPitIsRegularPit() {
        GameActionResult result = new GameActionResult(game.newInstance(), game.getPits().get(4));
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        Game updatedGame = ongoingGameState.execute(game.newInstance(), gameAction);

        assertThat(updatedGame.getCurrentPlayer().getName()).isEqualTo(game.getNextPlayer().getName());
    }

    @Test
    void executesFinishedGameStateWhenRowBecomesEmtpyAfterLastMove() {
        FinishedGameState mockedFinishedGameState = mock(FinishedGameState.class);
        OngoingGameState ongoingGameState = new OngoingGameState(mockedFinishedGameState);
        when(mockedFinishedGameState.isApplicable(any(Game.class))).thenReturn(true);

        GameActionResult result = new GameActionResult(game, game.getPits().get(4));
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        ongoingGameState.execute(game.newInstance(), gameAction);

        verify(mockedFinishedGameState).execute(any(Game.class), any(FinishGameAction.class));
    }

}
