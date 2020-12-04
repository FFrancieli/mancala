package kalah.game.models.pit;

import kalah.game.models.pit.PitType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PitTypeTest {

    @Test
    void returnsTrueWhenPitTypeIsKalah() {
        assertThat(PitType.KALAH.isKalah()).isTrue();
    }

    @Test
    void returnsFalsWhenPitTypeIsNotKalah() {
        assertThat(PitType.REGULAR.isKalah()).isFalse();
    }
}
