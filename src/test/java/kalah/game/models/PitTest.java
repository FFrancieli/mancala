package kalah.game.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PitTest {

    public static final int AMOUNT_OF_SEEDS = 4;

    @Test
    void amountOfSeedsIsEqualToZeroWhenNoInitialAmountIsGivenOnConstructor() {
        Pit pit = new Pit(PitType.KALAH, 6);

        assertThat(pit.getAmountOfSeeds()).isZero();
    }

    @Test
    void amountOfSeedsIsTheSameAsProvidedOnConstructor() {
        Pit pit = new Pit(PitType.REGULAR, 6, AMOUNT_OF_SEEDS);

        assertThat(pit.getAmountOfSeeds()).isEqualTo(AMOUNT_OF_SEEDS);
    }

    @Test
    void pitWithKalahTypeCanOnlyBeInitializedWithZeroSeeds() {
        assertThatThrownBy(() -> new Pit(PitType.KALAH, 1, AMOUNT_OF_SEEDS))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kalah must be initialized with zero seeds. Received 4 seeds on constructor");
    }
}
