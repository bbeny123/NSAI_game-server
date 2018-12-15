package pl.beny.nsai.game.checkers;

import pl.beny.nsai.game.checkers.CheckersMan.Side;
import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.*;
import static pl.beny.nsai.game.checkers.CheckersResult.Status.BLACK_TURN;
import static pl.beny.nsai.game.checkers.CheckersResult.Status.WHITE_TURN;
import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class CheckersBoard {

    private CheckersMan[][] board = new CheckersMan[8][8];

    public CheckersBoard() {
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

    private CheckersBoard(CheckersMan[][] board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null) {
                    this.board[x][y] = board[x][y].copy();
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

    public CheckersResult move(int x1, int y1, int x2, int y2, Side side) throws GamesException {
        return move(board(x1, y1), new CheckersMan(x2, y2), side);
    }

    private CheckersResult move(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        valid(source, target, side);

        List<CheckersMan> captureMoves = getCaptureMoves(source);

        CheckersResult result = new CheckersResult();
        result.setStatus(side == WHITE ? BLACK_TURN : WHITE_TURN);

        if (!captureMoves.isEmpty() && captureMoves.stream().noneMatch(move -> target.x == move.x && target.y == move.y)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED);
        } else if (!captureMoves.isEmpty()) {
            capture(source, target, result, side);
        } else {
            validMove(source, target, side);
            move(source, target, result);
        }

        return result;
    }

    private void valid(CheckersMan source, CheckersMan target, Side side) throws GamesException {
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
    }

    private void validMove(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        if (Math.abs(target.x - source.x) != 1 || Math.abs(target.y - source.y) != 1 || (source.type != QUEEN && ((side == WHITE && target.y >= source.y) || (side == BLACK && target.y <= source.y)))) {
            throw new GamesException(CHECKERS_NOT_ALLOWED);
        }
    }

    private void capture(CheckersMan source, CheckersMan target, CheckersResult result, Side side) throws GamesException {
        result.setCaptured(capture(source, target));
        move(source, target, result);

        List<CheckersMan> forceToCapture = getCaptureMoves(board(target));
        if (!forceToCapture.isEmpty()) {
            result.setForceToCapture(new CheckersMoves(board(target), forceToCapture));
            result.setStatus(side == WHITE ? WHITE_TURN : BLACK_TURN);
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

    private void move(CheckersMan source, CheckersMan target, CheckersResult result) {
        result.setSource(source.copy());
        board(source.x, source.y, null);
        board(target.x, target.y, source);
        source.move(target.x, target.y);
        result.setTarget(source.copy());
    }

    public List<CheckersMoves> getMoves(Side side, boolean captures) {
        List<CheckersMoves> moves = new ArrayList<>();
        getAllCheckers().forEach(c -> {
            if (c.side == side) {
                List<CheckersMan> men;
                if (captures) {
                    men = getCaptureMoves(c);
                } else {
                    men = getMoves(c);
                }
                if (!men.isEmpty()) {
                    moves.add(new CheckersMoves(c, men));
                }
            }
        });
        return moves;
    }

    private List<CheckersMan> getMoves(CheckersMan source) {
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

    private List<CheckersMan> getCaptureMoves(CheckersMan source) {
        List<CheckersMan> men = new ArrayList<>();

        //UPPER LEFT
        if (source.whiteOrQueen() && source.x - 2 >= 0 && source.y - 2 >= 0 && board(source.x - 2, source.y - 2) == null) {
            CheckersMan man = board(source.x - 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x - 2, source.y - 2, source.side, source.type));
            }
        }

        //UPPER RIGHT
        if (source.whiteOrQueen() && source.x + 2 <= 7 && source.y - 2 >= 0 && board(source.x + 2, source.y - 2) == null) {
            CheckersMan man = board(source.x + 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x + 2, source.y - 2, source.side, source.type));
            }
        }

        //BOTTOM LEFT
        if (source.blackOrQueen() && source.x - 2 >= 0 && source.y + 2 <= 7 && board(source.x - 2, source.y + 2) == null) {
            CheckersMan man = board(source.x - 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x - 2, source.y + 2, source.side, source.type));
            }
        }

        //BOTTOM RIGHT
        if (source.blackOrQueen() && source.x + 2 <= 7 && source.y + 2 <= 7 && board(source.x + 2, source.y + 2) == null) {
            CheckersMan man = board(source.x + 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x + 2, source.y + 2, source.side, source.type));
            }
        }

        return men;
    }

    public boolean moveOrCapturePossible(Side side) {
        return !getMoves(side, false).isEmpty() || !getMoves(side, true).isEmpty();
    }

    public void checkForcedCapture(int x1, int y1, int x2, int y2, CheckersMoves forcedCapture) throws GamesException {
        if (forcedCapture.getSource().x != x1 || forcedCapture.getSource().y != y1 || forcedCapture.getMoves().stream().noneMatch(move -> move.x == x2 && move.y == y2)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED_STATE);
        }
    }

    public List<CheckersMan> getAllCheckers() {
        List<CheckersMan> men = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = y % 2; x < 8; x += 2) {
                if (board(x, y) != null) {
                    men.add(board(x, y));
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

}