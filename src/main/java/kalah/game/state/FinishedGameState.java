package kalah.game.state;

import kalah.game.models.Game;
import kalah.game.state.action.GameAction;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FinishedGameState implements GameState {

    @Override
    public Game execute(Game game, GameAction gameAction) {
        if (isApplicable(game)) {
            return gameAction.performAction(game).getGame();
        }

        return game;
    }

    @Override
    public boolean isApplicable(Game game) {
        return game.isAnyRowEmpty();
    }
}
