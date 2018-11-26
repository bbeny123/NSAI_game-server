package pl.beny.nsai.game.checkers;

import java.util.ArrayList;
import java.util.List;

public class CheckersMove {

    private CheckersMan source;
    private List<CheckersMan> target;

    public CheckersMove(CheckersMan source, List<CheckersMan> target) {
        this.source = source;
        this.target = target;
    }

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public List<CheckersMan> getTarget() {
        if (target == null) {
            target = new ArrayList<>();
        }
        return target;
    }

    public void setTarget(List<CheckersMan> target) {
        this.target = target;
    }
}
