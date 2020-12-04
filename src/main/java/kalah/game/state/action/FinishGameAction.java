package kalah.game.state.action;

import kalah.game.models.Game;
import kalah.game.state.FinishedGameState;

public class FinishGameAction implements GameAction {
    @Override
    public GameActionResult performAction(Game game) {
        game.finish();
        game.setGameState(new FinishedGameState());

        return new GameActionResult(game);
    }
}
