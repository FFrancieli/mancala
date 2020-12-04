package kalah.game.models.pit;

import kalah.game.models.BoardSide;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PitsInitializer {

    private static final int MAXIMUM_OF_PITS_PER_SIDE_OF_BOARD = 7;

    public static List<Pit> initializePits(int amountOfSeeds) {
        Supplier<Stream<Pit>> pitsOnSouth = initializePits(amountOfSeeds, BoardSide.SOUTH);
        Supplier<Stream<Pit>> pitsOnNorth = initializePits(amountOfSeeds, BoardSide.NORTH);

        return Stream.concat(pitsOnSouth.get(), pitsOnNorth.get())
                .collect(Collectors.toList());
    }

    private static Supplier<Stream<Pit>> initializePits(int amountOfSeeds, BoardSide boardSide) {
        int firstPitIndex = boardSide.getFirstPitIndex();
        int kalahIndex = boardSide.getKalahIndex();

        return () -> IntStream.iterate(firstPitIndex, i -> i + 1)
                .limit(MAXIMUM_OF_PITS_PER_SIDE_OF_BOARD)
                .mapToObj(currentIndex -> getPit(amountOfSeeds, kalahIndex, currentIndex));
    }

    private static Pit getPit(int amountOfSeeds, int kalahIndex, int index) {
        if (index == kalahIndex) {
            return new Pit(PitType.KALAH, index);
        }

        return new Pit(PitType.REGULAR, index, amountOfSeeds);
    }
}
