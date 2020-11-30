package kalah.game.models;

import kalah.game.models.board.Board;
import kalah.game.models.board.BoardSide;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@RedisHash("Game")
@NoArgsConstructor
public class Game implements Serializable {

    @Id
    private String id;
    private Player firstPlayer;
    private Player secondPlayer;
    private Board board;
    private Player currentPlayer;

    public Game(String firstPlayerName, String secondPlayerName, int amountOfSeedsOnPit) {
        this.firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        this.secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.board = new Board(amountOfSeedsOnPit);
        this.currentPlayer = firstPlayer;
    }
}
