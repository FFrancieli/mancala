package assertions.custom;

import kalah.game.models.Pit;
import kalah.game.models.PitType;
import org.assertj.core.api.AbstractAssert;

public class PitAssert extends AbstractAssert<PitAssert, Pit> {

    public PitAssert(Pit actualPit) {
        super(actualPit, PitAssert.class);
    }

    public static PitAssert assertThat(Pit actual) {
        return new PitAssert(actual);
    }

    public PitAssert isKalah() {
        isNotNull();

        if (!actual.getPitType().isKalah()) {
            failWithMessage("Expected pit type to be <%s> but was <%s>", PitType.KALAH, actual.getPitType());
        }

        return this;
    }

    public PitAssert isRegularPit() {
        isNotNull();

        if (actual.getPitType().isKalah()) {
            failWithMessage("Expected pit type to be <%s> but was <%s>", PitType.REGULAR, actual.getPitType());
        }

        return this;
    }

    public PitAssert amountOfSeedsIs(int expectedAmountOfSeeds) {
        isNotNull();

        if (actual.getAmountOfSeeds() != expectedAmountOfSeeds) {
            failWithMessage("Expected pit to have <%d> seeds but it had <%d>", expectedAmountOfSeeds, actual.getAmountOfSeeds());
        }

        return this;
    }

    public PitAssert indexIs(int expectedIndex) {
        isNotNull();

        if (actual.getIndex() != expectedIndex) {
            failWithMessage("Expected pit index to be <%d>  but it was <%d>", expectedIndex, actual.getIndex());

        }

        return this;
    }

    public PitAssert hasZeroSeeds() {
        if (actual.getAmountOfSeeds() != 0) {
            failWithMessage("Expected pit to have 0 seeds but it has %d", actual.getAmountOfSeeds());
        }

        return this;
    }

}
