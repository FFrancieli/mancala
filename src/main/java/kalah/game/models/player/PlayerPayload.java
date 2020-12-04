package kalah.game.models.player;

import com.fasterxml.jackson.annotation.JsonProperty;
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
