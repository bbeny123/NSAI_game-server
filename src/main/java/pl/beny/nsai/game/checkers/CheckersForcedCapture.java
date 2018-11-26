package pl.beny.nsai.game.checkers;

import java.util.List;

public class CheckersForcedCapture {

    private CheckersMan source;
    private List<CheckersMan> toCapture;

    public CheckersForcedCapture(CheckersMan source, List<CheckersMan> toCapture) {
        this.source = source;
        this.toCapture = toCapture;
    }

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public List<CheckersMan> getToCapture() {
        return toCapture;
    }

    public void setToCapture(List<CheckersMan> toCapture) {
        this.toCapture = toCapture;
    }
}
