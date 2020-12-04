package kalah.game.models.pit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PitPayload {
    private final int totalSeeds;
    private final int index;

    @JsonCreator
    public PitPayload(@JsonProperty("totalSeeds") int totalSeeds, @JsonProperty("index") int index) {
        this.totalSeeds = totalSeeds;
        this.index = index;
    }

    public static PitPayload fromEntity(Pit pit) {
        return new PitPayload(pit.getAmountOfSeeds(), pit.getIndex());
    }
}
