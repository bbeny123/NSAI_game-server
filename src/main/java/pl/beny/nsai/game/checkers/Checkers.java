package pl.beny.nsai.game.checkers;

import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.checkers.CheckersResult.Turn;
import pl.beny.nsai.util.GamesException;

import java.util.Random;

import static pl.beny.nsai.game.checkers.Checkers.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersResult.Turn.COMPUTER_WON;
import static pl.beny.nsai.game.checkers.CheckersResult.Turn.PLAYER_WON;
import static pl.beny.nsai.util.GamesException.GamesErrors.CHECKERS_COMPUTER_TURN;
import static pl.beny.nsai.util.GamesException.GamesErrors.CHECKERS_PLAYER_TURN;

public class Checkers {

    public enum Side {
        WHITE,
        BLACK;

        Side() {
        }
    }

    private CheckersBoard board = new CheckersBoard();
    private int turn = Turn.PLAYER;
    private CheckersForcedCapture forcedCapture;

    public CheckersResult move(CheckersRequest request) throws GamesException {
        if (turn == Turn.COMPUTER) {
            throw new GamesException(CHECKERS_COMPUTER_TURN);
        }

        if (forcedCapture != null) {
            board.checkForcedCapture(request.getX1(), request.getY1(), request.getX2(), request.getY2(), forcedCapture);
        }

        CheckersResult result = board.move(request.getX1(), request.getY1(), request.getX2(), request.getY2(), Turn.PLAYER);
        turn = result.getTurn();
        forcedCapture = result.getForceToCapture();

        CheckersMove move = board.randomMove(forcedCapture, BLACK);
        if (turn == Turn.COMPUTER && move == null) {
            result.setTurn(PLAYER_WON);
            return result;
        } else if (turn == Turn.COMPUTER) {
            return move(result);
        }

        return result;
    }

    public CheckersResult move(CheckersResult result) throws GamesException {
        if (turn == Turn.PLAYER) {
            throw new GamesException(CHECKERS_PLAYER_TURN);
        }

        CheckersMove move = board.randomMove(forcedCapture, BLACK);
        CheckersResult computerResult;
        while (true) {
            try {
                CheckersMan target = move.getTarget().get(new Random().nextInt(move.getTarget().size()));
                computerResult = board.move(move.getSource().x, move.getSource().y, target.x, target.y, Turn.COMPUTER);
                computerResult.setComputerCaptured(computerResult.getCaptured());
                computerResult.setCaptured(result.getCaptured());
                turn = computerResult.getTurn();
                forcedCapture = computerResult.getForceToCapture();
                break;
            } catch (Exception e) {

            }
        }

        move = board.randomMove(forcedCapture, Side.WHITE);
        if (turn == Turn.PLAYER && move == null) {
            result.setTurn(COMPUTER_WON);
            return result;
        }

        return result;
    }

}
