package kalah.game.models;

import assertions.custom.PitAssert;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    private static final String FIRST_PLAYER_NAME = "John";
    private static final String SECOND_PLAYER_NAME = "Jane";
    private static final int SEEDS_ON_PIT = 5;

    private static final int TOTAL_AMOUNT_OF_KALAH = 2;
    private static final int TOTAL_AMOUNT_REGULAR_PITS = 12;
    private static final int TOTAL_AMOUNT_OF_PITS = 14;

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);
    }

    @Test
    void initializesListOfPits() {
        assertThat(game.getPits()).isNotNull()
                .isNotEmpty()
                .hasSize(TOTAL_AMOUNT_OF_PITS);
    }

    @Test
    void mustHave12PitsWithGivenNumberOfSeedsPerPit() {
        List<Pit> regularPits = game.getPits()
                .stream()
                .filter(pit -> !pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(regularPits).hasSize(TOTAL_AMOUNT_REGULAR_PITS);

        regularPits.forEach(pit ->
                PitAssert.assertThat(pit).amountOfSeedsIs(SEEDS_ON_PIT));
    }

    @Test
    void mustHaveTwoKalahWithoutSeeds() {
        List<Pit> kalah = game.getPits()
                .stream()
                .filter(pit -> pit.getPitType().isKalah())
                .collect(Collectors.toList());

        assertThat(kalah).hasSize(TOTAL_AMOUNT_OF_KALAH);

        kalah.forEach(pit -> PitAssert.assertThat(pit).hasZeroSeeds());
    }

    @Test
    void createsFirstPlayerAssignedToSouthSideOfBoardOnGameInstantiation() {
        Player firstPlayer = game.getPlayers().get(0);

        assertThat(firstPlayer).isNotNull();
        assertThat(firstPlayer.getName()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(firstPlayer.getBoardSide()).isEqualTo(BoardSide.SOUTH);
    }

    @Test
    void createsSecondPlayerAssignedToNorthSideOfBoardOnGameInstantiation() {
        Player secondPlayer = game.getPlayers().get(1);

        assertThat(secondPlayer).isNotNull();
        assertThat(secondPlayer.getName()).isEqualTo(SECOND_PLAYER_NAME);
        assertThat(secondPlayer.getBoardSide()).isEqualTo(BoardSide.NORTH);
    }

    @Test
    void createsPitsWithCorrectAmountOfSeedsPerPitOnGameInstantiation() {
        long pitsWithCorrectAmountPfSeeds = game.getPits()
                .stream()
                .filter(pit -> pit.getAmountOfSeeds() == SEEDS_ON_PIT)
                .count();


        assertThat(pitsWithCorrectAmountPfSeeds).isEqualTo(12);
    }

    @Test
    void initialGameIsOngoing() {
        assertThat(game.getStatus()).isEqualTo(GameStatus.ONGOING);
    }

    @Test
    void firstPlayerStartTheGame() {
        Player currentPlayer = game.getCurrentPlayer();

        assertThat(currentPlayer).isEqualTo(game.getPlayers().get(0));
    }

    @Test
    void secondPlayerIsSelectedAsNextPlayerWhenFirstPlayerIsCurrentPlayer() {
        Player nextPlayer = game.getNextPlayer();

        assertThat(nextPlayer).isEqualTo(game.getPlayers().get(1));
    }

    @Test
    void firstPlayerIsSelectedAsNextPlayerWhenSecondPlayerIsCurrentPlayer() {
        game.setCurrentPlayer(game.getPlayers().get(1));

        Player nextPlayer = game.getNextPlayer();

        assertThat(nextPlayer).isEqualTo(game.getPlayers().get(0));
    }

    @Test
    void returnsFalseWhenNoPitRowIsEmpty() {
        assertThat(game.isAnyRowEmpty()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void returnsTrueWhenEntireRowIsEmpty(BoardSide boardSide) {
        IntStream.iterate(boardSide.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        assertThat(game.isAnyRowEmpty()).isTrue();
    }

    @Test
    void gameStatusIsSetToFinishedWhenGameEnds() {
        game.finish();

        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void allSeedsOnNonEmptySideOfTheBoardGoesToThatSidesKalah(BoardSide boardSide) {
        IntStream.iterate(boardSide.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        game.finish();

        Pit kalahOnNonEmptyRow = game.getPits().get(boardSide.getOpositeSideKalahIndex());

        assertThat(kalahOnNonEmptyRow.getAmountOfSeeds()).isEqualTo(SEEDS_ON_PIT * 6);
    }

    @Test
    void firstPlayerWinsTheGameWhenThereAreMoreSeedsOnTheirKalah() {
        IntStream.iterate(BoardSide.NORTH.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        game.finish();

        assertThat(game.getWinner()).isEqualTo(FIRST_PLAYER_NAME);
    }

    @Test
    void secondPlayerWinsTheGameWhenThereAreMoreSeedsOnTheirKalah() {
        IntStream.iterate(BoardSide.SOUTH.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        game.finish();

        assertThat(game.getWinner()).isEqualTo(SECOND_PLAYER_NAME);
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void returnTrueWhenAllPitsOnGivenBoardSideAreEmpty(BoardSide boardSide) {
        IntStream.iterate(boardSide.getFirstPitIndex(), i -> i + 1).limit(7)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        assertThat(game.areAllPitsInRowEmpty(boardSide)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void returnsFalseWhenNotAllPitsOnGivenBoardSideAreEmpty(BoardSide boardSide) {
        IntStream.iterate(boardSide.getFirstPitIndex(), i -> i + 2).limit(3)
                .forEach(i -> game.getPits().get(i).removeAllSeeds());

        assertThat(game.areAllPitsInRowEmpty(boardSide)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void returnsFalseWhenThereAreNoEmptyPitsOnGivenBoardSide(BoardSide boardSide) {
        assertThat(game.areAllPitsInRowEmpty(boardSide)).isFalse();
    }

    @Test
    void clonesGame() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_ON_PIT);
        Game clonedGame = game.newInstance();

        assertThat(game).isNotEqualTo(clonedGame);

        assertThat(clonedGame.getId()).isEqualTo(game.getId());
        assertThat(clonedGame.getCurrentPlayer()).isEqualTo(game.getCurrentPlayer());
        assertThat(clonedGame.getPlayers()).isEqualTo(game.getPlayers());
        assertThat(clonedGame.getPits()).isEqualTo(game.getPits());
        assertThat(clonedGame.getStatus()).isEqualTo(game.getStatus());
        assertThat(clonedGame.getWinner()).isEqualTo(game.getWinner());
        assertThat(clonedGame.getGameState()).isEqualTo(game.getGameState());
    }
}
