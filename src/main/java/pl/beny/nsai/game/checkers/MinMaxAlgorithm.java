package pl.beny.nsai.game.checkers;


import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static pl.beny.nsai.game.checkers.CheckersMan.Side;

public class MinMaxAlgorithm {
    private int depth;
    private Side side;

    public MinMaxAlgorithm(Side s) {
        this.depth = 3;
        this.side = s;
    }

    public MinMaxAlgorithm(Side s, int depth) {
        this.side = s;
        this.depth = depth;
    }

    public CheckersResult makeMove(CheckersBoard board) {
        CheckersMoves m = minMaxStart(board, depth, side, true);

        try {
            CheckersResult decision = board.move(m.getSource().x, m.getSource().y, m.getMoves().get(0).x, m.getMoves().get(0).y, side);
        } catch (GamesException e) {
            e.printStackTrace();
        }

        return null;
    }

    private CheckersMoves minMaxStart(CheckersBoard board, int depth, Side side, boolean maximizingPlayer) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        List<CheckersMoves> possibleMoves;
        possibleMoves = board.getMoves(side, false);


        List<Double> heuristics = new ArrayList<>();
        if (possibleMoves.isEmpty())
            return null;

        CheckersBoard tempBoard = null;
        for (int i = 0; i < possibleMoves.size(); i++) {
            tempBoard = board.copy();
            try {
                tempBoard.move(possibleMoves.get(i).getSource().x, possibleMoves.get(i).getSource().y, possibleMoves.get(i).getMoves().get(i).x, possibleMoves.get(i).getMoves().get(i).y, side);
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
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    private double minMaxAlgorithm(CheckersBoard board, int depth, Side side, boolean maxOrMin, double alpha, double beta) {
        if (depth == 0) {
            return getHeuristic(board);
        }
        List<CheckersMoves> possibleMoves = board.getMoves(side, false);

        double initial = 0;
        CheckersBoard tempBoard = null;
        if (maxOrMin) {
            initial = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board.copy();
                try {
                    tempBoard.move(possibleMoves.get(i).getSource().x, possibleMoves.get(i).getSource().y, possibleMoves.get(i).getMoves().get(i).x, possibleMoves.get(i).getMoves().get(i).y, side);
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
                    tempBoard.move(possibleMoves.get(i).getSource().x, possibleMoves.get(i).getSource().y, possibleMoves.get(i).getMoves().get(i).x, possibleMoves.get(i).getMoves().get(i).y, side);
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

        return initial;
    }

    private double getHeuristic(CheckersBoard b) {
        double queenWeight = 2;
        double pieceWeight = 1;
        double result = 0;
        if (side == Side.WHITE)
            result = getNumWhiteQueenPieces(b) * queenWeight + getNumWhiteNormalPieces(b) * queenWeight - getNumBlackQueenPieces(b) * queenWeight - getNumBlackNormalPieces(b) * queenWeight;
        else
            result = getNumBlackQueenPieces(b) * queenWeight + getNumBlackNormalPieces(b) * queenWeight - getNumWhiteQueenPieces(b) * queenWeight - getNumWhiteNormalPieces(b) * queenWeight;
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
