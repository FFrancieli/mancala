package kalah.game.models;

import kalah.game.models.board.BoardSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static assertions.custom.PitAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PitTest {

    public static final int AMOUNT_OF_SEEDS = 4;
    public static final int INDEX = 6;

    private Pit pit;

    @BeforeEach
    void setUp() {
        pit = new Pit(PitType.REGULAR, INDEX, AMOUNT_OF_SEEDS);
    }

    @Test
    void amountOfSeedsIsEqualToZeroWhenNoInitialAmountIsGivenOnConstructor() {
        Pit pit = new Pit(PitType.KALAH, INDEX);

        assertThat(pit).hasZeroSeeds();
    }

    @Test
    void amountOfSeedsIsTheSameAsProvidedOnConstructor() {
        assertThat(pit).amountOfSeedsIs(AMOUNT_OF_SEEDS);
    }

    @Test
    void pitWithKalahTypeCanOnlyBeInitializedWithZeroSeeds() {
        assertThatThrownBy(() -> new Pit(PitType.KALAH, 1, AMOUNT_OF_SEEDS))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kalah must be initialized with zero seeds. Received 4 seeds on constructor");
    }

    @ParameterizedTest
    @MethodSource("oppositePitIndexMapping")
    void calculatesOppositePitIndex(int currentIndex, int oppositeIndex) {
        Pit pit = new Pit(PitType.REGULAR, currentIndex);

        assertThat(pit.findOppositePitIndex()).isEqualTo(oppositeIndex);
    }

    private static Stream<Arguments> oppositePitIndexMapping() {
        return Stream.of(
                Arguments.of(0, 12),
                Arguments.of(1, 11),
                Arguments.of(2, 10),
                Arguments.of(3, 9),
                Arguments.of(4, 8),
                Arguments.of(5, 7),
                Arguments.of(7, 5),
                Arguments.of(8, 4),
                Arguments.of(9, 3),
                Arguments.of(10, 2),
                Arguments.of(11, 1),
                Arguments.of(12, 0)
        );
    }

    @ParameterizedTest
    @EnumSource(value = BoardSide.class)
    void returnsOppositeKalahPositionWhenPitIsKalah(BoardSide boardSide) {
        Pit pit = new Pit(PitType.KALAH, boardSide.getKalahIndex());

        assertThat(pit.findOppositePitIndex()).isEqualTo(boardSide.getOpositeSideKalahIndex());
    }

    @Test
    void shouldSowOnePitIntoPit() {
        pit.sow();

        assertThat(pit).amountOfSeedsIs(AMOUNT_OF_SEEDS + 1);
    }

    @Test
    void addsSeedsToPit() {
        Pit pit = new Pit(PitType.KALAH, BoardSide.NORTH.getKalahIndex());

        pit.addSeeds(12);

        assertThat(pit).amountOfSeedsIs(12);
    }

    @Test
    void amountOfSeedsIsZeroWhenAllSeedsAreRemoved() {
        pit.removeAllSeeds();

        assertThat(pit).hasZeroSeeds();
    }

    @Test
    void clonesPit() {
        Pit clone = Pit.newInstance(pit);

        assertThat(clone).isNotEqualTo(pit);
        assertThat(clone).amountOfSeedsIs(pit.getAmountOfSeeds());
        assertThat(clone).indexIs(pit.getIndex());
        assertThat(clone.getPitType()).isEqualTo(pit.getPitType());
    }

    @Test
    void returnsTrueWhenPitHasOneOrMoreSeedsSeeds() {
        assertThat(pit.hasSeeds()).isTrue();
    }

    @Test
    void returnsFalseWhenPitHasZeroSeeds() {
        pit.setAmountOfSeeds(0);

        assertThat(pit.hasSeeds()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("pitsAssignedToPlayer")
    void pitIsAssignedToPlayerWhenIndexIsWithinPlayersBoardSide(BoardSide boardSide, Pit pit) {
        Player player = new Player("player", boardSide);

        assertThat(pit.isAssignedTo(player)).isTrue();
    }

    private static Stream<Arguments> pitsAssignedToPlayer() {
        return Stream.of(
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 0)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 1)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 2)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 3)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 4)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 5)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.KALAH, 6)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 7)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 8)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 9)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 10)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 11)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 12)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.KALAH, 13))
        );
    }

    @ParameterizedTest
    @MethodSource("pitsNotAssignedToPlayer")
    void pitIsNotAssignedToPlayerWhenIndexIsOutOfPlayersBoardSideRange(BoardSide boardSide, Pit pit) {
        Player player = new Player("player", boardSide);

        assertThat(pit.isAssignedTo(player)).isFalse();
    }

    private static Stream<Arguments> pitsNotAssignedToPlayer() {
        return Stream.of(
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 0)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 1)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 2)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 3)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 4)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.REGULAR, 5)),
                Arguments.of(BoardSide.NORTH, new Pit(PitType.KALAH, 6)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 7)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 8)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 9)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 10)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 11)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.REGULAR, 12)),
                Arguments.of(BoardSide.SOUTH, new Pit(PitType.KALAH, 13))
        );
    }
}
