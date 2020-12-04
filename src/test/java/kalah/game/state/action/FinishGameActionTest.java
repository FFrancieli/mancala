package kalah.game.state.action;

import kalah.game.models.game.Game;
import kalah.game.models.game.GameStatus;
import kalah.game.state.FinishedGameState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FinishGameActionTest {
    private static final String FIRST_PLAYER_NAME = "John";
    private static final String SECOND_PLAYER_NAME = "Jane";
    private static final int SEEDS_ON_PIT = 5;

    @Test
    void shouldFinishGame() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        FinishGameAction finishGameAction = new FinishGameAction();

        Game finishedGame = finishGameAction.performAction(game).getGame();

        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    void lastUpdatedPitIsAbsent() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        FinishGameAction finishGameAction = new FinishGameAction();

        GameActionResult result = finishGameAction.performAction(game);

        assertThat(result.getLastUpdatedPit()).isEmpty();
    }

    @Test
    void gameStateIsSetToFinisGameState() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);

        FinishGameAction finishGameAction = new FinishGameAction();
        Game finishedGame = finishGameAction.performAction(game).getGame();


        assertThat(finishedGame.getGameState()).isInstanceOf(FinishedGameState.class);
    }
}
