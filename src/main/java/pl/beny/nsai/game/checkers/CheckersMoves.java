package pl.beny.nsai.game.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckersMoves {

    private CheckersMan source;
    private List<CheckersMan> moves;

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

    public List<CheckersMan> getTargetsPositions() {
        return moves.stream().map(move -> new CheckersMan((source.x + move.x) / 2, (source.y + move.y) / 2)).collect(Collectors.toList());
    }

    public void setMoves(List<CheckersMan> moves) {
        this.moves = moves;
    }
}
