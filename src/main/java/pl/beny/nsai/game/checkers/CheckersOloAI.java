package pl.beny.nsai.game.checkers;

import java.util.List;
import java.util.Random;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

public class CheckersOloAI {

    public static CheckersResult moveAI(CheckersBoard board, CheckersForcedCapture forcedCapture) {
        while (true) {
            try {
                CheckersPossibleMoves move = getRandomMove(board, forcedCapture);
                CheckersMan target = move.getPossibleTargets().get(new Random().nextInt(move.getPossibleTargets().size()));

                CheckersResult result = board.move(move.getSource().x, move.getSource().y, target.x, target.y, BLACK);
                result.setComputerSource(move.getSource());
                result.setComputerTarget(target);
                result.setComputerCaptured(result.getPlayerCaptured());
                result.setPlayerCaptured(null);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static CheckersPossibleMoves getRandomMove(CheckersBoard board, CheckersForcedCapture forcedCapture) {
        if (forcedCapture != null) {
            return new CheckersPossibleMoves(forcedCapture.getSource(), forcedCapture.getPossibleCaptures());
        }

        List<CheckersPossibleMoves> moves = board.getPossibleMoves(BLACK);
        List<CheckersPossibleMoves> captures = board.getPossibleCaptures(BLACK);

        if (!captures.isEmpty() && (moves.isEmpty() || new Random().nextInt(2) == 0)) {
            return captures.get(new Random().nextInt(captures.size()));
        } else if (!moves.isEmpty()) {
            return moves.get(new Random().nextInt(moves.size()));
        } else {
            return null;
        }
    }

}
