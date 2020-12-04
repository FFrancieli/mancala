package kalah.game.state.action;

import kalah.game.models.game.Game;

public interface GameAction {
    GameActionResult performAction(Game game);
}
