package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersResult.Status.WHITE_TURN;

public class CheckersResult {

    public interface Status {
        int WHITE_TURN = 0;
        int WHITE_WON = 1;
        int BLACK_TURN = 2;
        int BLACK_WON = 3;
        int TIE = 4;
    }

    private CheckersMan source;
    private CheckersMan target;
    private CheckersMan captured;
    private CheckersMoves forceToCapture;
    private int status = WHITE_TURN;

    public CheckersMan getSource() {
        return source;
    }

    public void setSource(CheckersMan source) {
        this.source = source;
    }

    public CheckersMan getTarget() {
        return target;
    }

    public void setTarget(CheckersMan target) {
        this.target = target;
    }

    public CheckersMan getCaptured() {
        return captured;
    }

    public void setCaptured(CheckersMan captured) {
        this.captured = captured;
    }

    public CheckersMoves getForceToCapture() {
        return forceToCapture;
    }

    public void setForceToCapture(CheckersMoves forceToCapture) {
        this.forceToCapture = forceToCapture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
