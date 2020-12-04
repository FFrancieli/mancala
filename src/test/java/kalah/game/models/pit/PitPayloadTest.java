package kalah.game.models.pit;

import kalah.game.models.pit.Pit;
import kalah.game.models.pit.PitPayload;
import kalah.game.models.pit.PitType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PitPayloadTest {

    public static final int INDEX = 2;
    public static final int AMOUNT_OF_SEEDS = 6;

    @Test
    void shouldConvertFromPitEntity() {
        Pit entity = new Pit(PitType.REGULAR, INDEX, AMOUNT_OF_SEEDS);

        PitPayload payload = PitPayload.fromEntity(entity);

        assertThat(payload.getIndex()).isEqualTo(INDEX);
        assertThat(payload.getTotalSeeds()).isEqualTo(AMOUNT_OF_SEEDS);
    }
}
