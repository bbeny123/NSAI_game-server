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

//main Checkers class
public class Checkers extends Game {

    //available game difficulties
    public enum Difficulty {
        OloAI,
        MinMax,
        MinMaxFuzzy
    }

    private CheckersBoard board = new CheckersBoard();  //instance of checkers board
    private Difficulty difficulty = Difficulty.OloAI;   //game difficulty (values: Difficulty)
    private CheckersMoves forcedCapture;                //contains data of forced move
    private int status = WHITE_TURN;                    //indicates game status (values: pl.beny.nsai.game.checkers.CheckersResult.Status)

    //validates and executes player moveRequest
    public CheckersResult move(CheckersRequest request) throws GamesException {
        if (status == BLACK_TURN) {                             //check if it is player turn
            throw new GamesException(CHECKERS_COMPUTER_TURN);
        }
        if (forcedCapture != null) {                            //check if requested move is equals to one of the forced move (if move is forced)
            board.checkForcedCapture(request.getX1(), request.getY1(), request.getX2(), request.getY2(), forcedCapture);
        }

        return endTurn(board.move(request.getX1(), request.getY1(), request.getX2(), request.getY2(), WHITE));
    }

    //AI move (dependent on difficulty)
    @Override
    public List<Object> moveAI() throws GamesException {
        List<Object> results = new ArrayList<>();

        for (int i = 0; i < 1000 && status == BLACK_TURN; i++) {
            CheckersResult result = null;

            if (difficulty == Difficulty.OloAI) {
                result = CheckersOloAI.moveAI(board, forcedCapture);
            } else if (difficulty == Difficulty.MinMax) {
                //temp
                result = new MinMaxAlgorithm(BLACK).makeMove(board, forcedCapture);
            } else if (difficulty == Difficulty.MinMaxFuzzy) {
                result = new MinMaxAlgorithm(BLACK, true).makeMove(board, forcedCapture);
            }

            results.add(endTurn(result));
        }

        return results;
    }

    //sets and returns status on end of the turn
    private CheckersResult endTurn(CheckersResult result) {
        forcedCapture = result.getForceToCapture();
        status = result.getStatus();

        boolean whiteMoves = board.moveOrCapturePossible(WHITE);
        boolean blackMoves = board.moveOrCapturePossible(BLACK);

        if (board.getCheckers(WHITE).isEmpty()) {
            status = BLACK_WON;
        } else if (board.getCheckers(BLACK).isEmpty()) {
            status = WHITE_WON;
        } else if (status == WHITE_TURN && !whiteMoves) {
            status = BLACK_WON;
        } else if (status == BLACK_TURN && !blackMoves) {
            status = WHITE_WON;
        }

        result.setStatus(status);
        return result;
    }

    //set difficulty
    @Override
    public void setDifficulty(String difficultyLevel) {
        this.difficulty = Difficulty.valueOf(difficultyLevel);
    }
}
