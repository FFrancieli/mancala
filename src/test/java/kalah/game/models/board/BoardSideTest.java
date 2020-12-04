package kalah.game.models.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardSideTest {

    public static final int REGULAR_PIT_INDEX = 4;
    public static final int INVALID_PIT_INDEX = 15;

    @ParameterizedTest
    @MethodSource("boardSideKalahIndexMapping")
    void getsBoardSideByKalahIndex(int kalahIdex, BoardSide expectedBoardSide) {
        BoardSide boardSide = BoardSide.getSideByKalahIndex(kalahIdex);

        assertThat(boardSide).isEqualTo(expectedBoardSide);
    }

    private static Stream<Arguments> boardSideKalahIndexMapping() {
        return Stream.of(Arguments.of(BoardSide.SOUTH.getKalahIndex(), BoardSide.SOUTH),
                Arguments.of(BoardSide.NORTH.getKalahIndex(), BoardSide.NORTH));
    }

    @Test
    void throwsIllegalArgumentExceptionWhenIndexIsNotKalah() {
        assertThatThrownBy(() -> BoardSide.getSideByKalahIndex(REGULAR_PIT_INDEX))
                .hasMessage("There is no Kalah on board with index 4")
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("pitOIndexBoardSideMap")
    void returnsBoardSideByIndex(int pitIndex, BoardSide expectedBoardSide) {
        assertThat(BoardSide.getByPitIndex(pitIndex)).isEqualTo(expectedBoardSide);
    }

    public static Stream<Arguments> pitOIndexBoardSideMap() {
        return Stream.of(Arguments.of(0, BoardSide.SOUTH),
                Arguments.of(1, BoardSide.SOUTH),
                Arguments.of(2, BoardSide.SOUTH),
                Arguments.of(3, BoardSide.SOUTH),
                Arguments.of(4, BoardSide.SOUTH),
                Arguments.of(5, BoardSide.SOUTH),
                Arguments.of(6, BoardSide.SOUTH),
                Arguments.of(7, BoardSide.NORTH),
                Arguments.of(8, BoardSide.NORTH),
                Arguments.of(9, BoardSide.NORTH),
                Arguments.of(10, BoardSide.NORTH),
                Arguments.of(11, BoardSide.NORTH),
                Arguments.of(12, BoardSide.NORTH),
                Arguments.of(13, BoardSide.NORTH));
    }

    @Test
    void throwsIllegalArgumentExceptionWhenPitIndexIsInvalid() {
        assertThatThrownBy(() -> BoardSide.getByPitIndex(INVALID_PIT_INDEX))
                .hasMessage("Invalid pit index: 15");
    }
}
