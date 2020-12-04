package kalah.game.state;

import kalah.game.models.game.Game;
import kalah.game.models.BoardSide;
import kalah.game.state.action.GameAction;
import kalah.game.state.action.GameActionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinishedGameStateTest {
    @Mock
    GameAction gameAction;

    Game game;

    @BeforeEach
    void setUp() {
        game = new Game("player", "another player", 5);
    }

    FinishedGameState finishedGameState = new FinishedGameState();

    @Test
    void performsGameActionIfWhenEntireRowOfPitsIsEmpty() {
        clearPitsOnBoardSide();

        GameActionResult result = new GameActionResult(game);
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        finishedGameState.execute(game, gameAction);

        verify(gameAction).performAction(game);
    }

    @Test
    void returnsUpdatedGame() {
        clearPitsOnBoardSide();

        GameActionResult result = new GameActionResult(game);
        when(gameAction.performAction(any(Game.class))).thenReturn(result);

        Game updatedGame = finishedGameState.execute(game, gameAction);

        assertThat(updatedGame).isEqualTo(game);
    }

    @Test
    void gameActionIsNotPerformedWhenThereIsNoEmptyRowOfPits() {
        finishedGameState.execute(game, gameAction);

        verify(gameAction, never()).performAction(game);
    }

    private void clearPitsOnBoardSide() {
        IntStream.iterate(BoardSide.NORTH.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());
    }
}
