package pl.beny.nsai.game.checkers;

import pl.beny.nsai.util.GamesException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

//random AI
public class CheckersOloAI {

    //move AI checker from random to random place
    public static CheckersResult moveAI(CheckersBoard board, CheckersMoves forcedCapture) {
        for (int i = 0; i < 1000; i++) {    //fail-safe
            try {
                CheckersMoves move = getRandomMove(board, forcedCapture);   //get random possible move source

                CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));  //get random possible move target
                CheckersMan source = move.getSource();

                return board.move(source.x, source.y, target.x, target.y, BLACK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    //returns random possible move source
    private static CheckersMoves getRandomMove(CheckersBoard board, CheckersMoves forcedCapture) {
        //if move is forced than get random forced move
        if (forcedCapture != null) {
            return new CheckersMoves(forcedCapture.getSource(), forcedCapture.getMoves());
        }

        List<CheckersMoves> moves = board.getMoves(BLACK, true);            //get all possible capture moves
        if (!moves.isEmpty()) {
            return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));    //returns random capture move source
        }

        moves = board.getMoves(BLACK, false);                               //get all possible non capture moves
        if (!moves.isEmpty()) {
            return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));    //returns random non capture move source
        }

        return null;
    }

}
