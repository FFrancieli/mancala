package kalah.game.models.game;

import kalah.game.models.BoardSide;
import kalah.game.models.pit.Pit;
import kalah.game.models.pit.PitsInitializer;
import kalah.game.models.player.Player;
import kalah.game.state.GameState;
import kalah.game.state.OngoingGameState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@RedisHash("Game")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Game implements Serializable {

    @Id
    private String id;
    private Player currentPlayer;
    private List<Pit> pits;
    private List<Player> players;
    private GameStatus status;
    private String winner;
    private GameState gameState;

    public Game(String firstPlayerName, String secondPlayerName, int amountOfSeedsOnPit) {
        Player firstPlayer = new Player(firstPlayerName, BoardSide.SOUTH);
        Player secondPlayer = new Player(secondPlayerName, BoardSide.NORTH);
        this.players = List.of(firstPlayer, secondPlayer);

        this.currentPlayer = firstPlayer;
        this.pits = PitsInitializer.initializePits(amountOfSeedsOnPit);
        this.status = GameStatus.ONGOING;
        this.gameState = new OngoingGameState();
    }

    public Game newInstance() {
        return Game.builder()
                .id(this.id)
                .pits(this.pits)
                .players(this.players)
                .currentPlayer(this.currentPlayer)
                .winner(this.winner)
                .status(this.status)
                .gameState(this.gameState)
                .build();
    }

    public boolean isCurrentPlayerOpponentsKalah(int kalahIndex) {
        return this.currentPlayer.isOpponentPlayersKalah(kalahIndex);
    }

    public Player getNextPlayer() {
        int indexOfCurrentPlayer = players.indexOf(currentPlayer);

        return players.get(1 >> indexOfCurrentPlayer);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentPlayer() {
        this.currentPlayer = this.getNextPlayer();
    }

    public boolean isAnyRowEmpty() {
        return areAllPitsInRowEmpty(BoardSide.SOUTH) || areAllPitsInRowEmpty(BoardSide.NORTH);
    }

    private boolean areAllPitsOnNorthEmpty() {
        return areAllPitsInRowEmpty(BoardSide.NORTH);
    }

    public boolean areAllPitsInRowEmpty(BoardSide boardSide) {
        return calculateAmountOfSeedsOnRow(boardSide) == 0;
    }

    private int calculateAmountOfSeedsOnRow(BoardSide boardSide) {
        return this.getPits().stream()
                .filter(pit -> pit.getIndex() >= boardSide.getFirstPitIndex()
                        && pit.getIndex() < boardSide.getKalahIndex())
                .mapToInt(Pit::getAmountOfSeeds).sum();
    }

    public void finish() {
        this.status = GameStatus.FINISHED;

        if (areAllPitsOnNorthEmpty()) {
            captureSeedsIntoOppositeKalah(BoardSide.SOUTH);
        } else {
            captureSeedsIntoOppositeKalah(BoardSide.NORTH);
        }

        clearAllPits();
        defineWinner();
    }

    void captureSeedsIntoOppositeKalah(BoardSide boardSideWithSeeds) {
        int seedsLeftOnBoard = calculateAmountOfSeedsOnRow(boardSideWithSeeds);

        this.pits.get(boardSideWithSeeds.getKalahIndex()).addSeeds(seedsLeftOnBoard);
    }

    void clearAllPits() {
        this.pits.stream()
                .filter(pit -> !pit.isKalah())
                .forEach(Pit::removeAllSeeds);
    }

    private void defineWinner() {
        int amountOfSeedsNorth = this.pits.get(BoardSide.NORTH.getOpositeSideKalahIndex()).getAmountOfSeeds();
        int amountOfSeedsSouth = this.pits.get(BoardSide.SOUTH.getOpositeSideKalahIndex()).getAmountOfSeeds();

        this.winner = amountOfSeedsNorth > amountOfSeedsSouth ? players.get(0).getName() : players.get(1).getName();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
