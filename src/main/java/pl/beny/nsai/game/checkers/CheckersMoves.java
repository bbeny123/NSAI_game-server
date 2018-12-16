package pl.beny.nsai.game.checkers;

import java.util.ArrayList;
import java.util.List;

//contains possible moves for source CheckersMan
public class CheckersMoves {

    private CheckersMan source;         //source CheckersMan
    private List<CheckersMan> moves;    //possible moves

    public CheckersMoves(CheckersMan source, List<CheckersMan> moves) {
        this.source = source;
        this.moves = moves;
    }

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public List<CheckersMan> getMoves() {
        if (moves == null) {
            moves = new ArrayList<>();
        }
        return moves;
    }

    public void setMoves(List<CheckersMan> moves) {
        this.moves = moves;
    }
}
