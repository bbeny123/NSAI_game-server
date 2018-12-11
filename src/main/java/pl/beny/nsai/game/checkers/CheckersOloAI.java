package pl.beny.nsai.game.checkers;

import pl.beny.nsai.util.GamesException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

public class CheckersOloAI {

    public static CheckersResult moveAI(CheckersBoard board, CheckersMoves forcedCapture) {
        for (int i = 0; i < 1000; i++) {
            try {
                CheckersMoves move = getRandomMove(board, forcedCapture);

                CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
                CheckersMan source = move.getSource();

                return board.move(source.x, source.y, target.x, target.y, BLACK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    private static CheckersMoves getRandomMove(CheckersBoard board, CheckersMoves forcedCapture) {
        if (forcedCapture != null) {
            return new CheckersMoves(forcedCapture.getSource(), forcedCapture.getMoves());
        }

        List<CheckersMoves> moves = board.getMoves(BLACK, true);
        if (!moves.isEmpty()) {
            return moves.get(new Random().nextInt(moves.size()));
        }

        moves = board.getMoves(BLACK, false);
        if (!moves.isEmpty()) {
            return moves.get(new Random().nextInt(moves.size()));
        }

        return null;
    }

}
