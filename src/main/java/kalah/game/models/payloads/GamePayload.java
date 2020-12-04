package kalah.game.models.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kalah.game.models.Game;
import kalah.game.models.GameStatus;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ResponseBody
public class GamePayload {
    private final String id;
    private final String currentPlayer;

    @Size(min = 2, max = 2)
    private final List<PlayerPayload> players;

    @Size(min = 14, max = 14)
    private final List<PitPayload> pits;

    private final GameStatus gameStatus;
    private final String winner;

    @JsonCreator
    public GamePayload(@JsonProperty("id") String id, @JsonProperty("currentPlayer") String currentPlayer,
                       @JsonProperty("players") List<PlayerPayload> players, @JsonProperty("pits") List<PitPayload> pits,
                       @JsonProperty("gameStatus") GameStatus gameStatus, @JsonProperty("winner") String winner) {

        this.currentPlayer = currentPlayer;
        this.players = players;
        this.pits = pits;
        this.id = id;
        this.gameStatus = gameStatus;
        this.winner = winner;
    }

    public static GamePayload fromEnity(Game game) {
        List<PlayerPayload> players = convertPlayersToPayload(game);

        List<PitPayload> pits = convertPitsToPayload(game);

        return new GamePayload(game.getId(), game.getCurrentPlayer().getName(), players, pits, game.getStatus(), game.getWinner());
    }

    private static List<PlayerPayload> convertPlayersToPayload(Game game) {
        return List.of(
                PlayerPayload.fromEntity(game.getPlayers().get(0)),
                PlayerPayload.fromEntity(game.getPlayers().get(1)));
    }

    private static List<PitPayload> convertPitsToPayload(Game game) {
        return game.getPits()
                .stream()
                .map(PitPayload::fromEntity)
                .collect(Collectors.toList());
    }
}
