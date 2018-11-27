package pl.beny.nsai.game.checkers;

import java.util.List;

public class CheckersForcedCapture {

    private CheckersMan source;
    private List<CheckersMan> possibleCaptures;

    public CheckersForcedCapture(CheckersMan source, List<CheckersMan> possibleCaptures) {
        this.source = source;
        this.possibleCaptures = possibleCaptures;
    }

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public List<CheckersMan> getPossibleCaptures() {
        return possibleCaptures;
    }

    public void setPossibleCaptures(List<CheckersMan> possibleCaptures) {
        this.possibleCaptures = possibleCaptures;
    }
}
