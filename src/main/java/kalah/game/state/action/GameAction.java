package kalah.game.state.action;

import kalah.game.models.Game;

public interface GameAction {
    GameActionResult performAction(Game game);
}
