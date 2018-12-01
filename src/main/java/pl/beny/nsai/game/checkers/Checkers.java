package pl.beny.nsai.game.checkers;

import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.util.GamesException;

import static pl.beny.nsai.game.checkers.Checkers.Status.*;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.util.GamesException.GamesErrors.CHECKERS_COMPUTER_TURN;
import static pl.beny.nsai.util.GamesException.GamesErrors.CHECKERS_PLAYER_TURN;

public class Checkers {

    public interface Status {
        int WHITE_TURN = 0;
        int WHITE_WON = 1;
        int BLACK_TURN = 2;
        int BLACK_WON = 3;
        int TIE = 4;
    }

    private CheckersBoard board = new CheckersBoard();
    private CheckersPossibleMoves forcedCapture;
    private int status = Status.WHITE_TURN;

    public CheckersResult move(CheckersRequest request) throws GamesException {
        if (status == Status.BLACK_TURN) {
            throw new GamesException(CHECKERS_COMPUTER_TURN);
        }

        if (forcedCapture != null) {
            board.checkForcedCapture(request.getX1(), request.getY1(), request.getX2(), request.getY2(), forcedCapture);
        }

        CheckersResult result = board.move(request.getX1(), request.getY1(), request.getX2(), request.getY2(), WHITE);
        status = result.getStatus();
        forcedCapture = result.getForceToCapture();

        endTurn();
        result.setStatus(status);

        if (status == Status.BLACK_TURN) {
            CheckersResult computerResult = moveAI();
            result.setComputerSource(computerResult.getComputerSource());
            result.setComputerTarget(computerResult.getComputerTarget());
            result.setComputerCaptured(computerResult.getComputerCaptured());
            result.setStatus(status);
        }

        return result;
    }

    public CheckersResult moveAI() throws GamesException {
        if (status == Status.WHITE_TURN) {
            throw new GamesException(CHECKERS_PLAYER_TURN);
        }

        CheckersResult result = CheckersOloAI.moveAI(board, forcedCapture);
        status = result.getStatus();
        forcedCapture = result.getForceToCapture();
        result.setForceToCapture(null);

        endTurn();
        result.setStatus(status);

        return result;
    }


    private void endTurn() {
        boolean whiteMoves = board.moveOrCapturePossible(WHITE);
        boolean blackMoves = board.moveOrCapturePossible(BLACK);

        if (board.getCheckers(WHITE).isEmpty()) {
            status = BLACK_WON;
        } else if (board.getCheckers(BLACK).isEmpty()) {
            status = WHITE_WON;
        } else if (!whiteMoves && !blackMoves) {
            status = TIE;
        } else if (status == WHITE_TURN && !whiteMoves) {
            status = BLACK_TURN;
        } else if (status == BLACK_TURN && !blackMoves) {
            status = WHITE_TURN;
        }
    }

    public CheckersBoard copyBoard() {
        return board.copy();
    }

}
