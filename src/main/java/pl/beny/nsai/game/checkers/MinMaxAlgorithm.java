package pl.beny.nsai.game.checkers;


import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.game.checkers.CheckersMan.Side;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;

public class MinMaxAlgorithm {
    private int depth;
    private boolean fuzzy;
    private Side side;

    public MinMaxAlgorithm(Side s) {
        this.depth = 2;
        this.fuzzy = false;
        this.side = s;
    }

    public MinMaxAlgorithm(Side s, boolean fuzzy) {
        this.depth = 2;
        this.fuzzy = fuzzy;
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

        List<Double> heuristics = new ArrayList<>();

        CheckersBoard tempBoard = null;
        for (int i = 0; i < possibleMoves.size(); i++) {
            tempBoard = board.copy();
            move(side, possibleMoves, tempBoard, i);
            heuristics.add(minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;

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


        CheckersResult checkersResult = new CheckersResult();
        CheckersMoves move = possibleMoves.get(ThreadLocalRandom.current().nextInt(possibleMoves.size()));
        CheckersMan target = move.getMoves().get(0);
        CheckersMan source = move.getSource();
        checkersResult.setSource(source);
        checkersResult.setTarget(target);
        return checkersResult;
    }

    private double minMaxAlgorithm(CheckersBoard board, int depth, Side side, boolean maxOrMin, double alpha, double beta) {
        if (depth <= 0) {
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
                move(side, possibleMoves, tempBoard, i);

                double result = minMaxAlgorithm(tempBoard, depth - 1, flipSide(side), !maxOrMin, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        } else {
            initial = Double.POSITIVE_INFINITY;
            for (CheckersMoves possibleMove : possibleMoves) {
                tempBoard = board.copy();
                try {
                    CheckersMoves move = possibleMove;
                    CheckersMan target = move.getMoves().get(ThreadLocalRandom.current().nextInt(move.getMoves().size()));
                    CheckersMan source = move.getSource();
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


        return initial;
    }

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
