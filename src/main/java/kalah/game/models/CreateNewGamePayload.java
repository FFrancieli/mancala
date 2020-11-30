package kalah.game.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateNewGamePayload {
    private final String firstPlayerName;
    private final String secondPlayerName;

    public CreateNewGamePayload(@JsonProperty("first_player_name") String firstPlayerName,
                                @JsonProperty("second_player_name") String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }
}
