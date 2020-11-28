package kalah.game.models;

import kalah.game.models.board.BoardSide;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Player {

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
}
