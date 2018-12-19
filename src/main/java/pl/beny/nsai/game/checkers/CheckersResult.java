package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersResult.Status.WHITE_TURN;

//abstract representation of move result
public class CheckersResult {

    //possible game statuses
    public interface Status {
        int WHITE_TURN = 0;
        int WHITE_WON = 1;
        int BLACK_TURN = 2;
        int BLACK_WON = 3;
    }

    private CheckersMan source;             //move source CheckersMan
    private CheckersMan target;             //move target CheckersMan
    private CheckersMan captured;           //captured CheckersMan
    private CheckersMoves forceToCapture;   //forced moves
    private int status = WHITE_TURN;        //game status

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
