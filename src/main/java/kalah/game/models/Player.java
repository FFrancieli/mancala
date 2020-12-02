package kalah.game.models;

import kalah.game.models.board.BoardSide;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Player implements Serializable {

    private final String name;
    private final BoardSide boardSide;

    public Player(String name, BoardSide boardSide) {
        this.name = name;
        this.boardSide = boardSide;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;

        if (!Objects.equals(name, player.name)) return false;
        return boardSide == player.boardSide;
    }

    @Override
    public int hashCode() {
        int nameHashCode = name != null ? name.hashCode() : 0;
        int boardSideHashCode = boardSide != null ? boardSide.hashCode() : 0;

        return 31 * nameHashCode + boardSideHashCode;
    }

    public boolean isAssignedTo(Pit pit) {
        return pit.getIndex() >= boardSide.getFirstPitIndex() && boardSide.getKalahIndex() >= pit.getIndex();
    }

    public boolean isOpponentPlayersKalah(int otherKalahIndex) {
        int opponentsKalahIndex = this.getBoardSide().getOpositeSideKalahIndex();

        return opponentsKalahIndex == otherKalahIndex;
    }
}
