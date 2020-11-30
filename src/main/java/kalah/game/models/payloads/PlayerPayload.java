package kalah.game.models.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import kalah.game.models.Player;
import lombok.Getter;

@Getter
public class PlayerPayload {
    private final String name;
    private final String boardSide;

    public PlayerPayload(@JsonProperty("name") String name, @JsonProperty("boardSide") String boardSide) {
        this.name = name;
        this.boardSide = boardSide;
    }

    public static PlayerPayload fromEntity(Player player) {
        return new PlayerPayload(player.getName(), player.getBoardSide().name());
    }
}
