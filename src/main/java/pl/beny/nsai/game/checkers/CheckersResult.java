package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersResult.Turn.COMPUTER;

public class CheckersResult {

    public interface Turn {
        public int PLAYER = 0;
        public int COMPUTER = 1;
        public int PLAYER_WON = 2;
        public int COMPUTER_WON = 3;
    }

    private CheckersMan captured;
    private int turn = COMPUTER;
    private CheckersForcedCapture forceToCapture;
    private CheckersMan source;
    private CheckersMan target;
    private CheckersMan computerCaptured;

    public CheckersMan getCaptured() {
        return captured;
    }

    public void setCaptured(CheckersMan captured) {
        this.captured = captured;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public CheckersForcedCapture getForceToCapture() {
        return forceToCapture;
    }

    public void setForceToCapture(CheckersForcedCapture forceToCapture) {
        this.forceToCapture = forceToCapture;
    }

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

    public CheckersMan getComputerCaptured() {
        return computerCaptured;
    }

    public void setComputerCaptured(CheckersMan computerCaptured) {
        this.computerCaptured = computerCaptured;
    }
}
