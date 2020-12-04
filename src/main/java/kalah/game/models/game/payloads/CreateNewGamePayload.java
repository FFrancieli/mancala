package kalah.game.models.game.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateNewGamePayload {

    private final String firstPlayerName;
    private final String secondPlayerName;

    @JsonCreator
    public CreateNewGamePayload(@JsonProperty(value = "firstPlayerName", required = true) String firstPlayerName,
                                @JsonProperty(value = "secondPlayerName", required = true) String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }
}
