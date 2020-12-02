package kalah.game.models;

import kalah.game.models.board.BoardSide;
import kalah.game.models.board.PitsInitializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@RedisHash("Game")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Game implements Serializable {

    @Id
    private String id;
    private Player currentPlayer;
    private List<Pit> pits;
    private List<Player> players;

    public Game(String firstPlayerName, String secondPlayerName, int amountOfSeedsOnPit) {
        Player firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        Player secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.currentPlayer = firstPlayer;
        this.pits = PitsInitializer.initializePits(amountOfSeedsOnPit);
        this.players = List.of(firstPlayer, secondPlayer);
    }

    public boolean isCurrentPlayerOpponentsKalah(int kalahIndex) {
        return this.currentPlayer.isOpponentPlayersKalah(kalahIndex);
    }

    public Player getNextPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);

        return players.get(1 >> currentPlayerIndex);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
