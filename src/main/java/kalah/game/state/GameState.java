package kalah.game.state;

import kalah.game.models.game.Game;
import kalah.game.state.action.GameAction;

import java.io.Serializable;

public interface GameState extends Serializable {
    Game execute(Game game, GameAction gameAction);

    boolean isApplicable(Game game);
}
