package pl.beny.nsai.game.checkers;


import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.game.checkers.CheckersMan.Side;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

public class MinMaxAlgorithm {
    private int depth;
    private Side side;

    public MinMaxAlgorithm(Side s) {
        this.depth = 2;
        this.side = s;
    }

    public MinMaxAlgorithm(Side s, int depth) {
        this.side = s;
        this.depth = depth;
    }

    public CheckersResult makeMove(CheckersBoard board, CheckersMoves forcedCapture) {
        if (forcedCapture != null) {
            CheckersMoves move = new CheckersMoves(forcedCapture.getSource(), forcedCapture.getMoves());
            CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
            CheckersMan source = move.getSource();

            return board.move(source.x, source.y, target.x, target.y, BLACK);
        }

        CheckersResult result = minMaxStart(board, depth, side, true);

        System.out.printf("end move : %s %s %s %s %s\n", result.getSource().x, result.getSource().y, result.getTarget().x, result.getTarget().y, side);
        try {
            return board.move(result.getSource().x, result.getSource().y, result.getTarget().x, result.getTarget().y, side);
        } catch (GamesException e) {
            e.printStackTrace();
        }

        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    private CheckersResult minMaxStart(CheckersBoard board, int depth, Side side, boolean maximizingPlayer) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        List<CheckersMoves> possibleMoves = board.getMoves(side, true);

        if (possibleMoves.isEmpty())
            possibleMoves = board.getMoves(side, false);


        if (possibleMoves.isEmpty())
            return null;

//        List<CheckersMoves> moves = board.getMoves(BLACK, true);
//
//        if (moves.isEmpty()) {
//            moves = board.getMoves(BLACK, false);
//        }
//
//        if (!moves.isEmpty()) {
//            move = moves.get(new Random().nextInt(moves.size()));
//            CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
//            CheckersMan source = move.getSource();
//
//            CheckersResult result = new CheckersResult();
//            result.setSource(source);
//            result.setTarget(target);
//            return result;
//        }
//

        List<Double> heuristics = new ArrayList<>();

        CheckersBoard tempBoard = null;
        System.out.println("possibleMoves.size(): " + possibleMoves.size());
        System.out.println(Arrays.toString(possibleMoves.toArray()));
        for (int i = 0; i < possibleMoves.size(); i++) {
            tempBoard = board.copy();
            System.out.println(i);
            try {
                CheckersMoves move = possibleMoves.get(i);
                CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
                CheckersMan source = move.getSource();

                System.out.printf("move: %s %s %s %s %s\n", source.x, source.y, target.x, target.y, side);

                tempBoard.move(source.x, source.y, target.x, target.y, side);
            } catch (GamesException e) {
                e.printStackTrace();
            }

            heuristics.add(minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;

        Random rand = new Random();
        for (int i = heuristics.size() - 1; i >= 0; i--) {
            if (heuristics.get(i) >= maxHeuristics) {
                maxHeuristics = heuristics.get(i);
            }
        }

        for (int i = 0; i < heuristics.size(); i++) {
            if (heuristics.get(i) < maxHeuristics) {
                heuristics.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }

        System.out.println("possibleMoves.size(): " + possibleMoves.size());
        System.out.println(Arrays.toString(possibleMoves.toArray()));

        CheckersResult checkersResult = new CheckersResult();

        CheckersMoves move = possibleMoves.get(rand.nextInt(possibleMoves.size()));

        CheckersMan target = move.getMoves().get(0);
        CheckersMan source = move.getSource();

        checkersResult.setSource(source);
        checkersResult.setTarget(target);

        return checkersResult;
    }

    private double minMaxAlgorithm(CheckersBoard board, int depth, Side side, boolean maxOrMin, double alpha, double beta) {
        if (depth <= 0) {
            System.out.println("getHeuristic(board)");
            return getHeuristic(board);
        }

        List<CheckersMoves> possibleMoves = board.getMoves(side, true);
        if (possibleMoves.isEmpty())
            possibleMoves = board.getMoves(side, false);

        double initial = 0;
        CheckersBoard tempBoard = null;
        if (maxOrMin) {
            initial = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board.copy();
                try {
                    CheckersMoves move = possibleMoves.get(i);
                    CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
                    CheckersMan source = move.getSource();

                    System.out.printf("move: %s %s %s %s %s\n", source.x, source.y, target.x, target.y, side);

                    tempBoard.move(source.x, source.y, target.x, target.y, side);
                } catch (GamesException e) {
                    e.printStackTrace();
                }

                double result = minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), !maxOrMin, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        } else {
            initial = Double.POSITIVE_INFINITY;
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board.copy();
                try {
                    CheckersMoves move = possibleMoves.get(i);
                    CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
                    CheckersMan source = move.getSource();

                    System.out.printf("move: %s %s %s %s %s\n", source.x, source.y, target.x, target.y, side);

                    tempBoard.move(source.x, source.y, target.x, target.y, side);
                } catch (GamesException e) {
                    e.printStackTrace();
                }

                double result = minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), !maxOrMin, alpha, beta);

                initial = Math.min(result, initial);
                alpha = Math.min(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        }

        System.out.println("initial:  " + initial);
        return initial;
    }

    private double getHeuristic(CheckersBoard b) {
        double queenWeight = 2;
        double pieceWeight = 1;
        double result = 0;
        if (side == Side.WHITE)
            result = getNumWhiteQueenPieces(b) * queenWeight + getNumWhiteNormalPieces(b) * pieceWeight - getNumBlackQueenPieces(b) * queenWeight - getNumBlackNormalPieces(b) * pieceWeight;
        else
            result = getNumBlackQueenPieces(b) * queenWeight + getNumBlackNormalPieces(b) * pieceWeight - getNumWhiteQueenPieces(b) * queenWeight - getNumWhiteNormalPieces(b) * pieceWeight;
        return result;

    }

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
