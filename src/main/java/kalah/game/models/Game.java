package kalah.game.models;

import com.google.common.annotations.VisibleForTesting;
import kalah.game.models.board.BoardSide;
import kalah.game.models.board.PitsInitializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@RedisHash("Game")
@NoArgsConstructor
public class Game implements Serializable {

    @Id
    private String id;
    private Player firstPlayer;
    private Player secondPlayer;
    private Player currentPlayer;
    private List<Pit> pits;

    public Game(String firstPlayerName, String secondPlayerName, int amountOfSeedsOnPit) {
        this.firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        this.secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.currentPlayer = firstPlayer;
        this.pits = PitsInitializer.initializePits(amountOfSeedsOnPit);
    }

    @VisibleForTesting
    public Game(String firstPlayerName, String secondPlayerName, List<Pit> pits) {
        this.firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        this.secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.currentPlayer = firstPlayer;
        this.pits = pits;
    }

    public boolean isCurrentPlayerOpponentsKalah(int kalahIndex) {
        return this.currentPlayer.isOpponentPlayersKalah(kalahIndex);
    }

    @VisibleForTesting
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
