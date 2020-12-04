package kalah.game.state;

import kalah.game.models.game.Game;
import kalah.game.models.BoardSide;
import kalah.game.state.action.FinishGameAction;
import kalah.game.state.action.GameAction;
import kalah.game.state.action.GameActionResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
public class OngoingGameState implements GameState {

    private GameState nextState;

    public OngoingGameState() {
        this.nextState = new FinishedGameState();
    }

    @Override
    public Game execute(Game game, GameAction gameAction) {
        if (isApplicable(game)) {
            GameActionResult gameActionResult = gameAction.performAction(game);

            setNextPlayer(game, gameActionResult);
        }

        return nextState.isApplicable(game) ? nextState.execute(game, new FinishGameAction()) : game;
    }

    void setNextPlayer(Game game, GameActionResult gameActionResult) {
        gameActionResult.getLastUpdatedPit()
                .ifPresent(pit -> {
                    if (!pit.isKalah()) {
                        game.setCurrentPlayer();
                    }
                });
    }

    @Override
    public boolean isApplicable(Game game) {
        BoardSide playerBoardSide = game.getCurrentPlayer().getBoardSide();

        return !game.areAllPitsInRowEmpty(playerBoardSide);
    }
}
