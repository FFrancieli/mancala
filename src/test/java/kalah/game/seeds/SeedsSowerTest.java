package kalah.game.seeds;

import kalah.game.models.Pit;
import kalah.game.models.Player;
import kalah.game.models.board.BoarPitsInitializer;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static assertions.custom.PitAssert.assertThat;
import static kalah.game.models.board.BoardSide.NORTH;
import static kalah.game.models.board.BoardSide.SOUTH;

class SeedsSowerTest {

    private static final int AMOUNT_OF_SEEDS = 6;
    public static final Player PLAYER_SOUTH = new Player("player", BoardSide.SOUTH);
    public static final Player PLAYER_NORTH = new Player("player", NORTH);

    private List<Pit> pits;

    private SeedsSower seedsSower;


    @BeforeEach
    void setUp() {
        seedsSower = new SeedsSower();

        pits = BoarPitsInitializer.initializePits(AMOUNT_OF_SEEDS);
    }

    @Test
    void sowingPitOnIndexZeroForPlayerOnSouthEndsOnKalah() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(0));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        assertThat(sowedResult.getLastUpdatedPit()).isKalah()
                .indexIs(BoardSide.SOUTH.getKalahIndex())
                .amountOfSeedsIs(1);
    }

    @Test
    void pitOnIndexZeroSeedsAfterSowing() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(0));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        Pit pitSowedFrom = sowedResult.getUpdatedPits().get(0);

        assertThat(pitSowedFrom).hasZeroSeeds();
    }

    @Test
    void sowingPitOnIndexZeroForPlayerOnSouthDoesNotAffectNorthRowOnBoard() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(0));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        assertThat(sowedResult.getLastUpdatedPit())
                .isKalah()
                .amountOfSeedsIs(1);

        assertPitsAreCorrectlySetOnSideOfBoard(pits, NORTH);
    }

    @Test
    void skipsOpponentPlayersKalah() {
        pits.get(5).setAmountOfSeeds(10);
        pits.get(2).setAmountOfSeeds(1);

        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(5));
        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        Pit opponentsKalah = pits.get(NORTH.getKalahIndex());
        assertThat(opponentsKalah).hasZeroSeeds();

        Pit currentPlayersKalah = pits.get(BoardSide.SOUTH.getKalahIndex());
        assertThat(currentPlayersKalah).amountOfSeedsIs(1);
        assertThat(pits.get(5)).hasZeroSeeds();
        assertThat(pits.get(2)).amountOfSeedsIs(2);

        assertThat(sowedResult.getLastUpdatedPit()).indexIs(2);
    }

    @Test
    void sowingPitOnIndexSevenForPlayerOnNorthEndsOnKalah() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_NORTH, pits, pits.get(NORTH.getFirstPitIndex()));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        assertThat(sowedResult.getLastUpdatedPit())
                .isKalah()
                .amountOfSeedsIs(1)
                .indexIs(NORTH.getKalahIndex());
    }

    @Test
    void pitOnIndexSevenHasZeroSeedsAfterSowing() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(NORTH.getFirstPitIndex()));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        Pit pitSowedFrom = sowedResult.getUpdatedPits().get(NORTH.getFirstPitIndex());

        assertThat(pitSowedFrom).hasZeroSeeds();
    }

    @Test
    void sowingPitOnIndexSevenForPlayerOnNorthDoesNotAffectSouthRowOnBoard() {
        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_NORTH, pits, pits.get(NORTH.getFirstPitIndex()));

        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        assertThat(sowedResult.getLastUpdatedPit())
                .isKalah()
                .amountOfSeedsIs(1);

        assertPitsAreCorrectlySetOnSideOfBoard(pits, SOUTH);
    }

    @Test
    void skipsOpponentPlayersKalahWhenSowingFromNorthSideOfTheBoard() {
        pits.get(12).setAmountOfSeeds(10);
        pits.get(9).setAmountOfSeeds(1);

        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_NORTH, pits, pits.get(12));
        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        Pit opponentsKalah = pits.get(SOUTH.getKalahIndex());
        assertThat(opponentsKalah).hasZeroSeeds();

        Pit currentPlayersKalah = pits.get(NORTH.getKalahIndex());
        assertThat(currentPlayersKalah).amountOfSeedsIs(1);
        assertThat(pits.get(12)).hasZeroSeeds();
        assertThat(pits.get(9)).amountOfSeedsIs(2);

        assertThat(sowedResult.getLastUpdatedPit()).indexIs(9);
    }

    @Test
    void capturesSeedsFromOpponentWhenLastSeedEndsOnOwnedAndEmptyPit() {
        pits.get(SOUTH.getKalahIndex() - 1).setAmountOfSeeds(8);
        pits.get(SOUTH.getFirstPitIndex()).setAmountOfSeeds(0);

        SeedSowerDataWrapper seedSowerDataWrapper = new SeedSowerDataWrapper(PLAYER_SOUTH, pits, pits.get(5));
        SowingResult sowedResult = seedsSower.sow(seedSowerDataWrapper);

        assertThat(sowedResult.getLastUpdatedPit()).indexIs(0).hasZeroSeeds();

        Pit opponentsKalah = pits.get(SOUTH.getKalahIndex());
        assertThat(opponentsKalah).amountOfSeedsIs(9);
        assertThat(sowedResult.getUpdatedPits().get(12)).hasZeroSeeds();
        assertThat(sowedResult.getUpdatedPits().get(0)).hasZeroSeeds();
    }

    private void assertPitsAreCorrectlySetOnSideOfBoard(List<Pit> pits, BoardSide boardSide) {
        IntStream.range(boardSide.getFirstPitIndex(), boardSide.getKalahIndex())
                .forEach(index -> assertThat(pits.get(index)).amountOfSeedsIs(AMOUNT_OF_SEEDS));

        assertThat(pits.get(boardSide.getKalahIndex())).hasZeroSeeds();
    }
}
