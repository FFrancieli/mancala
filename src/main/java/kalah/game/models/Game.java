package kalah.game.models;

import kalah.game.models.board.Board;
import kalah.game.models.board.BoardSide;
import lombok.Getter;

@Getter
public class Game {
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Board board;
    private Player currentPlayer;

    public Game(String firstPlayerName, String secondPlayerName, int amountOfSeedsOnPit) {
        this.firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        this.secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.board = new Board(amountOfSeedsOnPit);
        this.currentPlayer = firstPlayer;
    }
}
