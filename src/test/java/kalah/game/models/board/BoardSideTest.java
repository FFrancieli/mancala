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
}
