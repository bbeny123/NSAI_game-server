package pl.beny.nsai.game.checkers;

import java.util.ArrayList;
import java.util.List;

public class CheckersPossibleMoves {

    private CheckersMan source;
    private List<CheckersMan> possibleTargets;

    public CheckersPossibleMoves(CheckersMan source, List<CheckersMan> possibleTargets) {
        this.source = source;
        this.possibleTargets = possibleTargets;
    }

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public List<CheckersMan> getPossibleTargets() {
        if (possibleTargets == null) {
            possibleTargets = new ArrayList<>();
        }
        return possibleTargets;
    }

    public void setPossibleTargets(List<CheckersMan> possibleTargets) {
        this.possibleTargets = possibleTargets;
    }
}
