package pl.beny.nsai.game.checkers;

import pl.beny.nsai.game.checkers.Checkers.Side;
import pl.beny.nsai.game.checkers.CheckersResult.Turn;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static pl.beny.nsai.game.checkers.Checkers.Side.BLACK;
import static pl.beny.nsai.game.checkers.Checkers.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersBoard.BoardStatus.*;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.MAN;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.QUEEN;
import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class CheckersBoard {

    private int[][] board = new int[7][7];

    public interface BoardStatus {
        int PROHIBITED = -1;
        int NOTHING = 0;
        int WHITE_MAN = 1;
        int WHITE_QUEEN = 2;
        int BLACK_MAN = 3;
        int BLACK_QUEEN = 4;
    }

    public CheckersBoard() {
        initBoard();
    }


    private void initBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = (i + 1) % 2; j < 8; j += 2) {
                board[i][j] = PROHIBITED;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                board[i][j] = BLACK_MAN;
            }
        }

        for (int i = 7; i > 4; i--) {
            for (int j = i % 2; j < 8; j += 2) {
                board[i][j] = WHITE_MAN;
            }
        }
    }

    private CheckersMan getMan(int x, int y) {
        switch (board[x][y]) {
            case WHITE_MAN:
                return new CheckersMan(x, y, WHITE, MAN);
            case WHITE_QUEEN:
                return new CheckersMan(x, y, WHITE, QUEEN);
            case BLACK_MAN:
                return new CheckersMan(x, y, BLACK, MAN);
            case BLACK_QUEEN:
                return new CheckersMan(x, y, BLACK, QUEEN);
        }
        return null;
    }

    public CheckersResult move(int x1, int y1, int x2, int y2, int turn) throws GamesException {
        return move(getMan(x1, y1), new CheckersMan(x2, y2), turn);
    }

    public CheckersResult move(CheckersMan source, CheckersMan target, int turn) throws GamesException {
        if (source == null) {
            throw new GamesException(CHECKERS_NO_MAN);
        }

        if (turn == Turn.PLAYER && source.side != WHITE) {
            throw new GamesException(CHECKERS_OPPOSITE_MAN);
        } else if (turn == Turn.COMPUTER && source.side != BLACK) {
            throw new GamesException(CHECKERS_ERROR);
        }

        if (board(target) == PROHIBITED) {
            throw new GamesException(CHECKERS_POSITION_PROHIBITED);
        }

        if (board(target) != NOTHING) {
            throw new GamesException(CHECKERS_POSITION_TAKEN);
        }

        List<CheckersMan> captures = getPossibleCaptures(source);

        CheckersResult result = new CheckersResult();
        result.setTurn(turn == Turn.PLAYER ? Turn.COMPUTER : Turn.PLAYER);

        if (!captures.isEmpty()) {
            if (captures.stream().noneMatch(capture -> target.x == capture.x && target.y == capture.y)) {
                throw new GamesException(CHECKERS_CAPTURE_FORCED);
            } else {
                result.setCaptured(capture(source, target));
                result.setTurn(turn);
                board[target.x][target.y] = getBoardStatus(source);
                List<CheckersMan> forceToCapture = getPossibleCaptures(getMan(target.x, target.y));

                if(!forceToCapture.isEmpty()) {
                    result.setForceToCapture(new CheckersForcedCapture(getMan(target.x, target.y), forceToCapture));
                }
            }
        } else {
            board[target.x][target.y] = getBoardStatus(source);
        }
        removeFromBoard(source);

        return result;
    }

    private CheckersMan capture(CheckersMan source, CheckersMan target) throws GamesException {
        CheckersMan captured = getMan((source.x + target.x) / 2, (source.y + target.y) / 2);

        if (captured == null) {
            throw new GamesException(CHECKERS_ERROR);
        }

        removeFromBoard(captured);
        return captured;
    }

    private int getBoardStatus(CheckersMan man) throws GamesException {
        if (man.side == WHITE && man.type == MAN) return WHITE_MAN;
        else if (man.side == WHITE && man.type == QUEEN) return WHITE_QUEEN;
        else if (man.side == BLACK && man.type == MAN) return BLACK_MAN;
        else if (man.side == BLACK && man.type == QUEEN) return BLACK_QUEEN;
        else throw new GamesException(CHECKERS_ERROR);
    }

    private void removeFromBoard(CheckersMan man) {
        board[man.x][man.y] = NOTHING;
    }

    private int board(CheckersMan men) {
        return board[men.x][men.y];
    }

    public List<CheckersMan> getPossibleCaptures(CheckersMan man) {
        List<CheckersMan> men = new ArrayList<>();

        //UPPER LEFT
        if (man.whiteOrQueen() && man.x - 2 >= 0 && man.y - 2 >= 0 && board[man.x - 2][man.y - 2] == NOTHING) {
            CheckersMan temp = getMan(man.x - 1, man.y - 1);
            if (temp != null && man.side != temp.side) {
                men.add(temp);
            }
        }

        //UPPER RIGHT
        if (man.whiteOrQueen() && man.x + 2 <= 7 && man.y - 2 >= 0 && board[man.x + 2][man.y - 2] == NOTHING) {
            CheckersMan temp = getMan(man.x + 1, man.y - 1);
            if (temp != null && man.side != temp.side) {
                men.add(temp);
            }
        }

        //BOTTOM LEFT
        if (man.blackOrQueen() && man.x - 2 >= 0 && man.y + 2 <= 7 && board[man.x - 2][man.y + 2] == NOTHING) {
            CheckersMan temp = getMan(man.x - 1, man.y + 1);
            if (temp != null && man.side != temp.side) {
                men.add(temp);
            }
        }

        //BOTTOM RIGHT
        if (man.blackOrQueen() && man.x + 2 <= 7 && man.y + 2 <= 7 && board[man.x + 2][man.y + 2] == NOTHING) {
            CheckersMan temp = getMan(man.x + 1, man.y + 1);
            if (temp != null && man.side != temp.side) {
                men.add(temp);
            }
        }

        return men;
    }

    public void checkForcedCapture(int x1, int y1, int x2, int y2, CheckersForcedCapture forcedCapture) throws GamesException {
        if (forcedCapture.getSource().x != x1 || forcedCapture.getSource().y != y1 || forcedCapture.getToCapture().stream().noneMatch(capture -> capture.x == x2 && capture.y == y2)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED);
        }
    }

    public List<CheckersMove> getPossibleMoves(Side side) {
        List<CheckersMove> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                List<CheckersMan> men = new ArrayList<>();
                if ((side == WHITE && (board[i][j] == WHITE_MAN || board[i][j] == WHITE_QUEEN)) || (side == BLACK && board[i][j] == BLACK_QUEEN)) {
                    if (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] == NOTHING) {
                        men.add(new CheckersMan(i - 1, j - 1));
                    } else if (i + 1 <= 7 && j - 1 >= 0 && board[i + 1][j - 1] == NOTHING) {
                        men.add(new CheckersMan(i + 1, j - 1));
                    }
                }
                if ((side == BLACK && (board[i][j] == BLACK_MAN || board[i][j] == BLACK_QUEEN)) || (side == WHITE && board[i][j] == WHITE_QUEEN)) {
                    if (i - 1 >= 0 && j + 1 <= 7 && board[i - 1][j + 1] == NOTHING) {
                        men.add(new CheckersMan(i - 1, j + 1));
                    } else if (i + 1 <= 7 && j + 1 <= 7 && board[i + 1][j + 1] == NOTHING) {
                        men.add(new CheckersMan(i + 1, j + 1));
                    }
                }
                if (!men.isEmpty()) {
                    CheckersMan source = new CheckersMan(i, j, side, Arrays.asList(WHITE_MAN, BLACK_MAN).contains(board[i][j]) ? MAN : QUEEN);
                    moves.add(new CheckersMove(source, men));
                }
            }
        }
        return moves;
    }

    public List<CheckersMove> getPossibleCaptures(Side side) {
        List<CheckersMove> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = i % 2; j < 8; j += 2) {
                CheckersMan man = getMan(i, j);
                if (man != null && man.side == side) {
                    List<CheckersMan> men = getPossibleCaptures(man);
                    if (!men.isEmpty()) {
                        moves.add(new CheckersMove(man, men));
                    }
                }
            }
        }
        return moves;
    }

    public CheckersMove randomMove(CheckersForcedCapture forcedCapture, Side side) {
        if (forcedCapture != null) {
            return new CheckersMove(forcedCapture.getSource(), forcedCapture.getToCapture());
        }

        List<CheckersMove> moves = getPossibleMoves(side);
        List<CheckersMove> captures = getPossibleCaptures(side);

        if (new Random().nextInt(2) == 0 && !captures.isEmpty()) {
            return captures.get(new Random().nextInt(captures.size()));
        } else if (!moves.isEmpty()) {
            return moves.get(new Random().nextInt(moves.size()));
        } else {
            return null;
        }
    }

}
