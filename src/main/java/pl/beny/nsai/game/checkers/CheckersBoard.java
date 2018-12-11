package pl.beny.nsai.game.checkers;

import pl.beny.nsai.game.checkers.CheckersMan.Side;
import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.beny.nsai.game.checkers.Checkers.Status.BLACK_TURN;
import static pl.beny.nsai.game.checkers.Checkers.Status.WHITE_TURN;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.*;
import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class CheckersBoard {

    private CheckersMan[][] board = new CheckersMan[8][8];

    public CheckersBoard() {
        initBoard();
    }

    private CheckersBoard(CheckersMan[][] board) {
        this.board = board;
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                if (board(i, j) != null) {
                    this.board[i][j] = board[i][j].copy();
                }
            }
        }
    }

    public CheckersBoard copy() {
        return new CheckersBoard(this.board);
    }

    private CheckersMan board(int x, int y) {
        return board[y][x];
    }

    private CheckersMan board(CheckersMan men) {
        return board[men.y][men.x];
    }

    private void board(int x, int y, CheckersMan men) {
        board[y][x] = men;
    }

    private void initBoard() {
        for (int y = 0; y < 8; y++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, null, PROHIBITED));
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = y % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, BLACK, MAN));
            }
        }

        for (int y = 7; y > 4; y--) {
            for (int x = y % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, WHITE, MAN));
            }
        }
    }

    public CheckersResult move(int x1, int y1, int x2, int y2, Side side) throws GamesException {
        return move(board(x1, y1), new CheckersMan(x2, y2), side);
    }

    private CheckersResult move(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        if (source == null || source.type == PROHIBITED) {
            throw new GamesException(CHECKERS_NO_MAN);
        }

        if (side != source.side) {
            throw new GamesException(CHECKERS_OPPOSITE_MAN);
        }

        if (board(target) != null && board(target).type == PROHIBITED) {
            throw new GamesException(CHECKERS_POSITION_PROHIBITED);
        } else if (board(target) != null) {
            throw new GamesException(CHECKERS_POSITION_TAKEN);
        }

        List<CheckersMan> possibleCaptures = getPossibleCaptures(source);

        CheckersResult result = new CheckersResult();
        result.setStatus(side == WHITE ? BLACK_TURN : WHITE_TURN);

        if (!possibleCaptures.isEmpty() && possibleCaptures.stream().noneMatch(capture -> (source.x + target.x) / 2 == capture.x && (source.y + target.y) / 2 == capture.y)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED);
        } else if (!possibleCaptures.isEmpty()) {
            capture(source, target, result, side);
        } else {
            moveAllowed(source, target, side);
            move(source, target);
        }

        return result;
    }

    private void capture(CheckersMan source, CheckersMan target, CheckersResult result, Side side) throws GamesException {
        result.setPlayerCaptured(capture(source, target));
        move(source, target);

        List<CheckersMan> forceToCapture = getPossibleCaptures(board(target));
        if (!forceToCapture.isEmpty()) {
            result.setStatus(side == WHITE ? WHITE_TURN : BLACK_TURN);
            result.setForceToCapture(new CheckersPossibleMoves(board(target), forceToCapture));
        }
    }

    private CheckersMan capture(CheckersMan source, CheckersMan target) throws GamesException {
        CheckersMan captured = board((source.x + target.x) / 2, (source.y + target.y) / 2);

        if (captured == null) {
            throw new GamesException(CHECKERS_ERROR);
        }

        board(captured.x, captured.y, null);
        return captured;
    }

    private void moveAllowed(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        if (Math.abs(target.x - source.x) != 1 || Math.abs(target.y - source.y) != 1 || (source.type != QUEEN && ((side == WHITE && target.y >= source.y) || (side == BLACK && target.y <= source.y)))) {
            throw new GamesException(CHECKERS_NOT_ALLOWED);
        }
    }

    private void move(CheckersMan source, CheckersMan target) {
        board(source.x, source.y, null);
        source.move(target);
        board(target.x, target.y, source);
    }

    public List<CheckersPossibleMoves> getPossibleMoves(Side side) {
        List<CheckersPossibleMoves> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                if (board(i, j) != null && board(i, j).side == side) {
                    List<CheckersMan> men = getPossibleMoves(board(i, j));
                    if (!men.isEmpty()) {
                        moves.add(new CheckersPossibleMoves(board(i, j), men));
                    }
                }
            }
        }
        return moves;
    }

    private List<CheckersMan> getPossibleMoves(CheckersMan source) {
        List<CheckersMan> men = new ArrayList<>();

        if (source.whiteOrQueen() && source.x - 1 >= 0 && source.y - 1 >= 0 && board(source.x - 1, source.y - 1) == null) {
            men.add(new CheckersMan(source.x - 1, source.y - 1, source.side, source.type));
        }
        if (source.whiteOrQueen() && source.x + 1 <= 7 && source.y - 1 >= 0 && board(source.x + 1, source.y - 1) == null) {
            men.add(new CheckersMan(source.x + 1, source.y - 1, source.side, source.type));
        }
        if (source.blackOrQueen() && source.x - 1 >= 0 && source.y + 1 <= 7 && board(source.x - 1, source.y + 1) == null) {
            men.add(new CheckersMan(source.x - 1, source.y + 1, source.side, source.type));
        }
        if (source.blackOrQueen() && source.x + 1 <= 7 && source.y + 1 <= 7 && board(source.x + 1, source.y + 1) == null) {
            men.add(new CheckersMan(source.x + 1, source.y + 1, source.side, source.type));
        }

        return men;
    }

    public List<CheckersPossibleMoves> getPossibleCaptures(Side side) {
        List<CheckersPossibleMoves> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                if (board(i, j) != null && board(i, j).side == side) {
                    List<CheckersMan> possibleCaptures = getPossibleCaptures(board(i, j));
                    if (!possibleCaptures.isEmpty()) {
                        moves.add(new CheckersPossibleMoves(board(i, j), possibleCaptures));
                    }
                }
            }
        }
        return moves;
    }

    private List<CheckersMan> getPossibleCaptures(CheckersMan source) {
        List<CheckersMan> men = new ArrayList<>();

        //UPPER LEFT
        if (source.whiteOrQueen() && source.x - 2 >= 0 && source.y - 2 >= 0 && board(source.x - 2, source.y - 2) == null) {
            CheckersMan man = board(source.x - 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(man);
            }
        }

        //UPPER RIGHT
        if (source.whiteOrQueen() && source.x + 2 <= 7 && source.y - 2 >= 0 && board(source.x + 2, source.y - 2) == null) {
            CheckersMan man = board(source.x + 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(man);
            }
        }

        //BOTTOM LEFT
        if (source.blackOrQueen() && source.x - 2 >= 0 && source.y + 2 <= 7 && board(source.x - 2, source.y + 2) == null) {
            CheckersMan man = board(source.x - 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(man);
            }
        }

        //BOTTOM RIGHT
        if (source.blackOrQueen() && source.x + 2 <= 7 && source.y + 2 <= 7 && board(source.x + 2, source.y + 2) == null) {
            CheckersMan man = board(source.x + 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(man);
            }
        }

        return men;
    }

    public boolean moveOrCapturePossible(Side side) {
        return !getPossibleMoves(side).isEmpty() || !getPossibleCaptures(side).isEmpty();
    }

    public void checkForcedCapture(int x1, int y1, int x2, int y2, CheckersPossibleMoves forcedCapture) throws GamesException {
        if (forcedCapture.getSource().x != x1 || forcedCapture.getSource().y != y1 || forcedCapture.getPossibleTargets().stream().noneMatch(capture -> capture.x == x2 && capture.y == y2)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED);
        }
    }

    public List<CheckersMan> getAllCheckers() {
        List<CheckersMan> men = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                if (board(i, j) != null) {
                    men.add(board(i, j));
                }
            }
        }
        return men;
    }

    public List<CheckersMan> getCheckers(Side side) {
        return getAllCheckers().stream().filter(checker -> checker.side == side).collect(Collectors.toList());
    }

    public List<CheckersMan> getCheckers(Side side, Type type) {
        return getCheckers(side).stream().filter(checker -> checker.side == side && checker.type == type).collect(Collectors.toList());
    }

    public double getNumWhiteQueenPieces() {
        return getCheckers(Side.WHITE, Type.QUEEN).size();
    }

    public double getNumBlackQueenPieces() {
        return getCheckers(Side.BLACK, Type.QUEEN).size();
    }

    public double getNumWhiteNormalPieces() {
        return getCheckers(Side.WHITE, Type.MAN).size();
    }

    public double getNumBlackNormalPieces() {
        return getCheckers(Side.BLACK, Type.MAN).size();
    }

}
