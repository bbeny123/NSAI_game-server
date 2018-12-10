package pl.beny.nsai.game.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void possibleTargetsToPlaces() {
        possibleTargets = possibleTargets.stream().map(t -> new CheckersMan(t.x + t.x - source.x, t.y + t.y - source.y, t.side, t.type)).collect(Collectors.toList());
    }

    public void setPossibleTargets(List<CheckersMan> possibleTargets) {
        this.possibleTargets = possibleTargets;
    }
}
