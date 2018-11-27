package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.Checkers.Status.WHITE_TURN;

public class CheckersResult {

    private CheckersMan playerCaptured;
    private CheckersForcedCapture forceToCapture;
    private CheckersMan computerSource;
    private CheckersMan computerTarget;
    private CheckersMan computerCaptured;
    private int status = WHITE_TURN;

    public CheckersMan getPlayerCaptured() {
        return playerCaptured;
    }

    public void setPlayerCaptured(CheckersMan playerCaptured) {
        this.playerCaptured = playerCaptured;
    }

    public CheckersForcedCapture getForceToCapture() {
        return forceToCapture;
    }

    public void setForceToCapture(CheckersForcedCapture forceToCapture) {
        this.forceToCapture = forceToCapture;
    }

    public CheckersMan getComputerSource() {
        return computerSource;
    }

    public void setComputerSource(CheckersMan computerSource) {
        this.computerSource = computerSource;
    }

    public CheckersMan getComputerTarget() {
        return computerTarget;
    }

    public void setComputerTarget(CheckersMan computerTarget) {
        this.computerTarget = computerTarget;
    }

    public CheckersMan getComputerCaptured() {
        return computerCaptured;
    }

    public void setComputerCaptured(CheckersMan computerCaptured) {
        this.computerCaptured = computerCaptured;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
