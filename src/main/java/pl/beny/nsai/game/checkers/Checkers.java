package pl.beny.nsai.game.checkers;

import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersResult.Status.*;
import static pl.beny.nsai.util.GamesException.GamesErrors.CHECKERS_COMPUTER_TURN;

public class Checkers extends Game {

    public enum Difficulty {
        OloAI,
        MinMax
    }

    private CheckersBoard board = new CheckersBoard();
    private Difficulty difficulty = Difficulty.OloAI;
    private CheckersMoves forcedCapture;
    private int status = WHITE_TURN;

    public CheckersResult move(CheckersRequest request) throws GamesException {
        if (status == BLACK_TURN) {
            throw new GamesException(CHECKERS_COMPUTER_TURN);
        }
        if (forcedCapture != null) {
            board.checkForcedCapture(request.getX1(), request.getY1(), request.getX2(), request.getY2(), forcedCapture);
        }

        return endTurn(board.move(request.getX1(), request.getY1(), request.getX2(), request.getY2(), WHITE));
    }

    @Override
    public List<Object> moveAI() throws GamesException {
        List<Object> results = new ArrayList<>();

        for (int i = 0; i < 1000 && status == BLACK_TURN; i++) {
            CheckersResult result = null;

            if (difficulty == Difficulty.OloAI) {
                result = CheckersOloAI.moveAI(board, forcedCapture);
            } else if (difficulty == Difficulty.MinMax) {
                result = new MinMaxAlgorithm(BLACK).makeMove(board, forcedCapture);
            }

            //result = new MinMaxAlgorithm(BLACK).makeMove(board, forcedCapture);

            results.add(endTurn(result));
        }

        return results;
    }

    private CheckersResult endTurn(CheckersResult result) {
        forcedCapture = result.getForceToCapture();
        status = result.getStatus();

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

        result.setStatus(status);
        return result;
    }

    @Override
    public void setDifficulty(String difficultyLevel) {
        this.difficulty = Difficulty.valueOf(difficultyLevel);
    }
}
