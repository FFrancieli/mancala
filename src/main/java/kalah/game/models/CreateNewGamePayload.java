package kalah.game.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateNewGamePayload {
    private final String firstPlayerName;
    private final String secondPlayerName;

    @JsonCreator
    public CreateNewGamePayload(@JsonProperty("firstPlayerName") String firstPlayerName,
                                @JsonProperty("secondPlayerName") String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }
}
