package pl.beny.nsai.game.checkers;


import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.game.checkers.CheckersMan.Side;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;


/**
 * This class provides checker's computer movement powered by MIN-MAX algorithm (with Fuzzy logic optionally)
 */
public class MinMaxAlgorithm {

    /**
     * Depth of min-max search tree. The bigger number the longer it takes to have an output
     */
    private int depth;

    /**
     * Use fuzzy logic if true
     */
    private boolean fuzzy;

    /**
     * Side of computer
     */
    private Side side;


    /**
     * @param s Side of AI
     */
    public MinMaxAlgorithm(Side s) {
        this.depth = 3;
        this.fuzzy = false;
        this.side = s;
    }

    /**
     * @param s     Side of AI
     * @param fuzzy True if using fuzzy logic
     */
    public MinMaxAlgorithm(Side s, boolean fuzzy) {
        this.depth = 3;
        this.fuzzy = fuzzy;
        this.side = s;
    }

    /**
     * Moves draughtsman on board.
     *
     * @param board         Reference to checkers board
     * @param forcedCapture If set move with forced capture
     * @return Returns CheckersResult with information where to move draughtsman
     */
    public CheckersResult makeMove(CheckersBoard board, CheckersMoves forcedCapture) {
        // Check if there are forced captures
        if (forcedCapture != null) {
            // Create forced moves
            CheckersMoves forcedMoves = new CheckersMoves(forcedCapture.getSource(), forcedCapture.getMoves());
            // Set source and get random target
            CheckersMan source = forcedMoves.getSource();
            CheckersMan target = forcedMoves.getMoves().get(ThreadLocalRandom.current().nextInt(forcedMoves.getMoves().size()));
            return board.move(source.x, source.y, target.x, target.y, BLACK);
        }

        // If there is no captured moves start min-max algorithm and find best and optimal move
        CheckersResult result = minMaxStart(board, depth, side);
        try {
            return board.move(result.getSource().x, result.getSource().y, result.getTarget().x, result.getTarget().y, side);
        } catch (GamesException e) {
            e.printStackTrace();
        }

        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    /**
     * Starts min-max algorithm.
     *
     * @param board Reference to checkers board
     * @param depth Value how far search in tree
     * @param side  Side of computer
     * @return Returns CheckersResult with information where to move draughtsman
     */
    private CheckersResult minMaxStart(CheckersBoard board, int depth, Side side) {
        // Start with maximizing
        boolean maximizingPlayer = true;

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        // Get all possible moves on board
        List<CheckersMoves> possibleMoves = board.getMoves(side, true);

        // Check if possible moves list is empty
        if (possibleMoves.isEmpty())
            // Set possible moves with different captures flag
            possibleMoves = board.getMoves(side, false);


        // If list is still empty return null and throw error
        if (possibleMoves.isEmpty())
            return null;

        // Initialize list of evaluated values
        List<Double> heuristics = new ArrayList<>();

        // Reference for copied board
        CheckersBoard tempBoard = null;
        for (int i = 0; i < possibleMoves.size(); i++) {
            tempBoard = board.copy();
            //Make move on temp board and do it recursively (depends on depth)
            move(side, possibleMoves, tempBoard, i);
            maximizingPlayer = !maximizingPlayer;
            heuristics.add(minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), maximizingPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;

        // Find max evaluated value
        for (int i = heuristics.size() - 1; i >= 0; i--) {
            if (heuristics.get(i) >= maxHeuristics) {
                maxHeuristics = heuristics.get(i);
            }
        }

        // Remove all values below max evaluated value
        for (int i = 0; i < heuristics.size(); i++) {
            if (heuristics.get(i) < maxHeuristics) {
                heuristics.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }

        // Return random move from all best moves
        CheckersResult checkersResult = new CheckersResult();
        CheckersMoves move = possibleMoves.get(ThreadLocalRandom.current().nextInt(possibleMoves.size()));
        checkersResult.setSource(move.getSource());
        checkersResult.setTarget(move.getMoves().get(0));
        return checkersResult;
    }

    /**
     * Searching tree with all possible moves to find the best and optimal move. Using evaluation function
     * algorithm searches for max path among the computer's moves and the minimum among the moves of the opponent.
     */
    private double minMaxAlgorithm(CheckersBoard board, int depth, Side side, boolean maxOrMin, double alpha, double beta) {
        // If depth is 0 return evaluated value
        if (depth <= 0) {
            return getHeuristic(board);
        }

        //Get all possible moves from copied
        List<CheckersMoves> possibleMoves = board.getMoves(side, true);
        if (possibleMoves.isEmpty())
            possibleMoves = board.getMoves(side, false);

        double initial = 0;
        CheckersBoard tempBoard = null;
        if (maxOrMin) {
            initial = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board.copy();
                move(side, possibleMoves, tempBoard, i);

                maxOrMin = !maxOrMin;
                double result = minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), maxOrMin, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        } else {
            initial = Double.POSITIVE_INFINITY;
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board.copy();
                move(side, possibleMoves, tempBoard, i);

                maxOrMin = !maxOrMin;
                double result = minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), maxOrMin, alpha, beta);

                initial = Math.min(result, initial);
                alpha = Math.min(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        }


        return initial;
    }

    // Makes move
    private void move(Side side, List<CheckersMoves> possibleMoves, CheckersBoard tempBoard, int i) {
        try {
            CheckersMoves move = possibleMoves.get(i);
            CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
            CheckersMan source = move.getSource();
            tempBoard.move(source.x, source.y, target.x, target.y, side);
        } catch (GamesException e) {
            e.printStackTrace();
        }
    }

    /**
     * Evaluation function that determines the current state. Using number of draughtsmans and the weight of the draughtsman (queen or normal)
     * (or with fuzzy logic that determines value of current stage of game) returns value.
     */
    private double getHeuristic(CheckersBoard b) {
        double queenWeight = 3;
        double pieceWeight = 1;
        double result = 0;
        if (side == Side.WHITE) {
            result = getNumWhiteQueenPieces(b) * queenWeight + getNumWhiteNormalPieces(b) * pieceWeight - getNumBlackQueenPieces(b) * queenWeight - getNumBlackNormalPieces(b) * pieceWeight;
            if (fuzzy) {
                try {
                    result += FuzzyLogic.getStageVal(getNumWhiteNormalPieces(b), getNumBlackNormalPieces(b), b.getTurns());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            result = getNumBlackQueenPieces(b) * queenWeight + getNumBlackNormalPieces(b) * pieceWeight - getNumWhiteQueenPieces(b) * queenWeight - getNumWhiteNormalPieces(b) * pieceWeight;
            if (fuzzy) {
                try {
                    result += FuzzyLogic.getStageVal(getNumBlackNormalPieces(b), getNumWhiteNormalPieces(b), b.getTurns());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Changes side of player for algorithm purposes
     */
    private Side flipSide(Side side) {
        if (side == Side.BLACK)
            return Side.WHITE;
        return Side.BLACK;
    }

    private double getNumWhiteQueenPieces(CheckersBoard board) {
        return board.getCheckers(Side.WHITE, Type.QUEEN).size();
    }

    private double getNumBlackQueenPieces(CheckersBoard board) {
        return board.getCheckers(Side.BLACK, Type.QUEEN).size();
    }

    private double getNumWhiteNormalPieces(CheckersBoard board) {
        return board.getCheckers(Side.WHITE, Type.MAN).size();
    }

    private double getNumBlackNormalPieces(CheckersBoard board) {
        return board.getCheckers(Side.BLACK, Type.MAN).size();
    }
}
