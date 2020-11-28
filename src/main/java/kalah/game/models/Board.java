package kalah.game.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {
    private final List<Pit> pits;

    public Board(int numberOfSeedsPerPit) {
        pits = initializePits(numberOfSeedsPerPit);
    }

    private List<Pit> initializePits(int amountOfSeeds) {
        List<Pit> pitsOnSouth = initializePits(amountOfSeeds, BoardSide.SOUTH);
        List<Pit> pitsOnNorth = initializePits(amountOfSeeds, BoardSide.NORTH);

        return Stream.concat(pitsOnSouth.stream(), pitsOnNorth.stream())
                .collect(Collectors.toList());
    }

    private List<Pit> initializePits(int amountOfSeeds, BoardSide boardSide) {
        int firstPitIndex = boardSide.getFirstPitIndex();
        int kalahIndex = boardSide.getKalahIndex();

        return IntStream.iterate(firstPitIndex, i -> i + 1).limit(7)
                .mapToObj(index -> getPit(amountOfSeeds, kalahIndex, index))
                .collect(Collectors.toList());
    }

    private Pit getPit(int amountOfSeeds, int kalahIndex, int index) {
        if (index == kalahIndex) {
            return new Pit(PitType.KALAH, index);
        }

        return new Pit(PitType.REGULAR, index, amountOfSeeds);
    }

    public List<Pit> getPits() {
        return pits;
    }
}
