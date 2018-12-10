package pl.beny.nsai.game.checkers;

import pl.beny.nsai.util.GamesException;

import java.util.List;
import java.util.Random;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

public class CheckersOloAI {

    public static CheckersResult moveAI(CheckersBoard board, CheckersPossibleMoves forcedCapture) {
        for (int i = 0; i < 1000; i++) {
            try {
                CheckersPossibleMoves move = getRandomMove(board, forcedCapture);
                CheckersMan target = move.getPossibleTargets().get(new Random().nextInt(move.getPossibleTargets().size()));
                CheckersMan source = new CheckersMan(move.getSource().x, move.getSource().y, BLACK, move.getSource().type);

                CheckersResult result = board.move(source.x, source.y, target.x, target.y, BLACK);
                result.setComputerSource(source);
                result.setComputerTarget(target);
                result.setComputerCaptured(result.getPlayerCaptured());
                result.setPlayerCaptured(null);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    private static CheckersPossibleMoves getRandomMove(CheckersBoard board, CheckersPossibleMoves forcedCapture) {
        if (forcedCapture != null) {
            return new CheckersPossibleMoves(forcedCapture.getSource(), forcedCapture.getPossibleTargets());
        }

        List<CheckersPossibleMoves> moves = board.getPossibleMoves(BLACK);
        List<CheckersPossibleMoves> captures = board.getPossibleCaptures(BLACK);

        if (!captures.isEmpty()) {
            CheckersPossibleMoves capture = captures.get(new Random().nextInt(captures.size()));
            capture.possibleTargetsToPlaces();
            return capture;
        } else if (!moves.isEmpty()) {
            return moves.get(new Random().nextInt(moves.size()));
        } else {
            return null;
        }
    }

}
