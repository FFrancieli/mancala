package kalah.game.seeds;

import kalah.game.models.Pit;
import kalah.game.models.Player;
import kalah.game.models.board.PitsInitializer;
import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SeedSowerDataWrapperTest {

    public static final String PLAYER_NAME = "Player";
    public static final int AMOUNT_OF_SEEDS = 6;
    public static final int INDEX_OF_REGULAR_PIT = 11;

    private static final List<Pit> PITS = PitsInitializer.initializePits(AMOUNT_OF_SEEDS);
    Player player = new Player(PLAYER_NAME, BoardSide.NORTH);

    @BeforeEach
    void setUp() {
        player = new Player(PLAYER_NAME, BoardSide.NORTH);
    }

    @Test
    void returnsFalseWhenGivenIndexRefersToCurrentPlayersKalah() {
        SeedSowerDataWrapper seedSowerData = new SeedSowerDataWrapper(player, PITS, PITS.get(0));

        assertThat(seedSowerData.isOpponentPlayersKalah(BoardSide.NORTH.getKalahIndex())).isFalse();
    }

    @Test
    void returnsFalseWhenGivenIndexDoesNotReferToKalah() {
        SeedSowerDataWrapper seedSowerData = new SeedSowerDataWrapper(player, PITS, PITS.get(0));

        assertThat(seedSowerData.isOpponentPlayersKalah(INDEX_OF_REGULAR_PIT)).isFalse();
    }

    @Test
    void returnsTrueWhenGivenIndexRefersToOpponentssKalah() {
        SeedSowerDataWrapper seedSowerData = new SeedSowerDataWrapper(player, PITS, PITS.get(0));

        assertThat(seedSowerData.isOpponentPlayersKalah(BoardSide.SOUTH.getKalahIndex())).isTrue();
    }

    @ParameterizedTest
    @EnumSource(BoardSide.class)
    void retrievesIndexOfKalahAssignedToCurrentPlayer(BoardSide boardSide) {
        Player player = new Player(PLAYER_NAME, boardSide);

        SeedSowerDataWrapper seedSowerData = new SeedSowerDataWrapper(player, PITS, PITS.get(0));
        int oppositeKalah = seedSowerData.getIndexOfCurrentPlayerKalah();

        assertThat(oppositeKalah).isEqualTo(boardSide.getKalahIndex());
    }
}

