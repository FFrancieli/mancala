package kalah.game.models;

import kalah.game.models.board.BoardSide;
import kalah.game.models.board.PitsInitializer;
import kalah.game.state.GameState;
import kalah.game.state.OngoingGameState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

@Getter
@RedisHash("Game")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Game implements Serializable {
    public static final Predicate<Pit> IS_PIT_ON_SOUTH_SIDE_OF_THE_BOARD = pit -> BoardSide.SOUTH.getKalahIndex() > pit.getIndex();
    public static final Predicate<Pit> IS_PIT_ON_NORTH_SIDE_OF_THE_BOARD = pit -> pit.getIndex() < BoardSide.NORTH.getKalahIndex()
            && pit.getIndex() >= BoardSide.NORTH.getFirstPitIndex();

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
        int currentPlayerIndex = players.indexOf(currentPlayer);

        return players.get(1 >> currentPlayerIndex);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentPlayer() {
        this.currentPlayer = this.getNextPlayer();
    }

    public boolean isAnyRowEmpty() {
        return areAllPitsOnSouthEmpty() || areAllPitsOnNorthEmpty();
    }

    private boolean areAllPitsOnNorthEmpty() {
        return allPitsEmpty(IS_PIT_ON_NORTH_SIDE_OF_THE_BOARD);
    }

    private boolean areAllPitsOnSouthEmpty() {
        return allPitsEmpty(IS_PIT_ON_SOUTH_SIDE_OF_THE_BOARD);
    }

    private boolean allPitsEmpty(Predicate<Pit> predicate) {
        return this.getPits().stream()
                .filter(predicate)
                .mapToInt(Pit::getAmountOfSeeds).sum() == 0;
    }

    public boolean areAllPitsInRowEmpty(BoardSide boardSide) {
        return this.getPits().stream()
                .filter(pit -> pit.getIndex() >= boardSide.getFirstPitIndex() && pit.getIndex() <= boardSide.getKalahIndex())
                .mapToInt(Pit::getAmountOfSeeds).sum() == 0;
    }

    public void finish() {
        this.status = GameStatus.FINISHED;

        if (areAllPitsOnNorthEmpty()) {
            int seedsLeftOnBoard = countSeedsOn(IS_PIT_ON_SOUTH_SIDE_OF_THE_BOARD);
            this.pits.get(BoardSide.NORTH.getOpositeSideKalahIndex()).addSeeds(seedsLeftOnBoard);
        } else {
            int seedsLeftOnBoard = countSeedsOn(IS_PIT_ON_NORTH_SIDE_OF_THE_BOARD);
            this.pits.get(BoardSide.SOUTH.getOpositeSideKalahIndex()).addSeeds(seedsLeftOnBoard);
        }

        clearAllPits();
        defineWinner();
    }

    void clearAllPits() {
        this.pits.stream().filter(pit -> !pit.isKalah()).forEach(Pit::removeAllSeeds);
    }

    private void defineWinner() {
        int amountOfSeedsNorth = this.pits.get(BoardSide.NORTH.getOpositeSideKalahIndex()).getAmountOfSeeds();
        int amountOfSeedsSouth = this.pits.get(BoardSide.SOUTH.getOpositeSideKalahIndex()).getAmountOfSeeds();

        this.winner = amountOfSeedsNorth > amountOfSeedsSouth ? players.get(0).getName() : players.get(1).getName();
    }

    private int countSeedsOn(Predicate<Pit> predicate) {
        return this.pits.stream()
                .filter(predicate)
                .mapToInt(Pit::getAmountOfSeeds).sum();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
