package kalah.game.state.action;

import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.models.Player;
import kalah.game.models.board.BoardSide;
import kalah.game.models.board.PitsInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static assertions.custom.PitAssert.assertThat;
import static kalah.game.models.board.BoardSide.NORTH;
import static kalah.game.models.board.BoardSide.SOUTH;

class SowSeedsActionTest {

    private static final int AMOUNT_OF_SEEDS = 6;
    private static final String PLAYER_SOUTH_OF_BOARD = "first_player";
    private static final String PLAYER_NORTH_OF_BOARD = "secondPlayer";
    public static final Player PLAYER_SOUTH = new Player(PLAYER_SOUTH_OF_BOARD, BoardSide.SOUTH);
    private static final Player PLAYER_NORTH = new Player(PLAYER_NORTH_OF_BOARD, NORTH);
    private static final int FIRST_PIT_ON_SOUTH_INDEX = 0;

    private List<Pit> pits;
    private Game game;

    private SowSeedsAction sowAction;

    @BeforeEach
    void setUp() {
        sowAction = new SowSeedsAction(FIRST_PIT_ON_SOUTH_INDEX);

        pits = PitsInitializer.initializePits(AMOUNT_OF_SEEDS);
        game = new Game(PLAYER_SOUTH_OF_BOARD, PLAYER_NORTH_OF_BOARD, AMOUNT_OF_SEEDS);
    }

    @Test
    void sowingPitOnIndexZeroForPlayerOnSouthEndsOnKalah() {
        GameActionResult sowedResult = sowAction.performAction(game);

        assertThat(sowedResult.getLastUpdatedPit().get()).isKalah()
                .indexIs(BoardSide.SOUTH.getKalahIndex())
                .amountOfSeedsIs(1);
    }

    @Test
    void pitOnIndexZeroSeedsAfterSowing() {
        GameActionResult sowedResult = sowAction.performAction(game);

        Pit pitSowedFrom = sowedResult.getGame().getPits().get(0);

        assertThat(pitSowedFrom).hasZeroSeeds();
    }

    @Test
    void sowingPitOnIndexZeroForPlayerOnSouthDoesNotAffectNorthRowOnBoard() {
        GameActionResult sowedResult = sowAction.performAction(game);

        assertThat(sowedResult.getLastUpdatedPit().get())
                .isKalah()
                .amountOfSeedsIs(1);

        assertPitsAreCorrectlySetOnSideOfBoard(pits, NORTH);
    }

    @Test
    void skipsOpponentPlayersKalah() {
        pits.get(5).setAmountOfSeeds(10);
        pits.get(2).setAmountOfSeeds(1);

        Game game = Game.builder().currentPlayer(PLAYER_SOUTH).pits(pits).build();

        SowSeedsAction sowSeedsAction = new SowSeedsAction(5);
        GameActionResult sowedResult = sowSeedsAction.performAction(game);

        Pit opponentsKalah = pits.get(NORTH.getKalahIndex());
        assertThat(opponentsKalah).hasZeroSeeds();

        Pit currentPlayersKalah = pits.get(BoardSide.SOUTH.getKalahIndex());
        assertThat(currentPlayersKalah).amountOfSeedsIs(1);
        assertThat(pits.get(5)).hasZeroSeeds();
        assertThat(pits.get(2)).amountOfSeedsIs(2);

        assertThat(sowedResult.getLastUpdatedPit().get()).indexIs(2);
    }

    @Test
    void sowingPitOnIndexSevenForPlayerOnNorthEndsOnKalah() {
        Game game = Game.builder()
                .currentPlayer(PLAYER_NORTH)
                .pits(pits).build();


        SowSeedsAction sowSeedsAction = new SowSeedsAction(NORTH.getFirstPitIndex());
        GameActionResult sowedResult = sowSeedsAction.performAction(game);

        assertThat(sowedResult.getLastUpdatedPit().get())
                .isKalah()
                .amountOfSeedsIs(1)
                .indexIs(NORTH.getKalahIndex());
    }

    @Test
    void pitOnIndexSevenHasZeroSeedsAfterSowing() {
        Game game = Game.builder()
                .currentPlayer(PLAYER_NORTH)
                .pits(pits).build();

        SowSeedsAction sowSeedsAction = new SowSeedsAction(NORTH.getFirstPitIndex());
        GameActionResult sowedResult = sowSeedsAction.performAction(game);

        Pit pitSowedFrom = sowedResult.getGame().getPits().get(NORTH.getFirstPitIndex());

        assertThat(pitSowedFrom).hasZeroSeeds();
    }

    @Test
    void sowingPitOnIndexSevenForPlayerOnNorthDoesNotAffectSouthRowOnBoard() {
        Game game = Game.builder()
                .currentPlayer(PLAYER_NORTH)
                .pits(pits).build();

        SowSeedsAction sowSeedsAction = new SowSeedsAction(NORTH.getFirstPitIndex());
        GameActionResult sowedResult = sowSeedsAction.performAction(game);


        assertThat(sowedResult.getLastUpdatedPit().get())
                .isKalah()
                .amountOfSeedsIs(1);

        assertPitsAreCorrectlySetOnSideOfBoard(pits, SOUTH);
    }

    @Test
    void skipsOpponentPlayersKalahWhenSowingFromNorthSideOfTheBoard() {
        pits.get(12).setAmountOfSeeds(10);
        pits.get(9).setAmountOfSeeds(1);

        Game game = Game.builder()
                .currentPlayer(PLAYER_NORTH)
                .pits(pits).build();

        SowSeedsAction sowSeedsAction = new SowSeedsAction(12);
        GameActionResult sowedResult = sowSeedsAction.performAction(game);


        Pit opponentsKalah = pits.get(SOUTH.getKalahIndex());
        assertThat(opponentsKalah).hasZeroSeeds();

        Pit currentPlayersKalah = pits.get(NORTH.getKalahIndex());
        assertThat(currentPlayersKalah).amountOfSeedsIs(1);
        assertThat(pits.get(12)).hasZeroSeeds();
        assertThat(pits.get(9)).amountOfSeedsIs(2);

        assertThat(sowedResult.getLastUpdatedPit().get()).indexIs(9);
    }

    @Test
    void capturesSeedsFromOpponentWhenLastSeedEndsOnOwnedAndEmptyPit() {
        pits.get(SOUTH.getKalahIndex() - 1).setAmountOfSeeds(8);
        pits.get(SOUTH.getFirstPitIndex()).setAmountOfSeeds(0);

        Game game = Game.builder()
                .currentPlayer(PLAYER_SOUTH)
                .pits(pits).build();

        SowSeedsAction sowSeedsAction = new SowSeedsAction(5);
        GameActionResult sowedResult = sowSeedsAction.performAction(game);


        assertThat(sowedResult.getLastUpdatedPit().get()).indexIs(0).hasZeroSeeds();

        Pit opponentsKalah = pits.get(SOUTH.getKalahIndex());
        assertThat(opponentsKalah).amountOfSeedsIs(9);
        assertThat(sowedResult.getGame().getPits().get(12)).hasZeroSeeds();
        assertThat(sowedResult.getGame().getPits().get(0)).hasZeroSeeds();
    }

    private void assertPitsAreCorrectlySetOnSideOfBoard(List<Pit> pits, BoardSide boardSide) {
        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> assertThat(pits.get(index)).amountOfSeedsIs(AMOUNT_OF_SEEDS));

        assertThat(pits.get(boardSide.getKalahIndex())).hasZeroSeeds();
    }
}
