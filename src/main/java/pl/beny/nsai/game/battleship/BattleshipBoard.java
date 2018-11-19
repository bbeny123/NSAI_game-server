package pl.beny.nsai.game.battleship;

import pl.beny.nsai.util.GamesException;

import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_OFF_THE_BOARD;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_PLACE_FIRED;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_WRONG_PLACEMENT;

public class BattleshipBoard {

    public final static int BOARD_SIZE = 10;

    public interface BoardStatus {
        int NOTHING = 0;
        int SHIP = 1;
        int SHIP_HIT = 2;
        int FIRED = 3;
    }

    private interface FireStatus {
        int MISS = 0;
        int HIT = -1;
    }

    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    public void placeShip(int x1, int y1, int x2, int y2) throws GamesException {
        inBoard(x1, y1, x2, y2);
        placeAvailable(x1, y1, x2, y2);

        for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
            for (int j = Math.min(y1, y2); j <= Math.max(y1, y2); j++) {
                board[i][j] = BoardStatus.SHIP;
            }
        }
    }

    public int fire(int x, int y) throws GamesException {
        inBoard(x, y);
        alreadyFired(x, y);
        if (board[x][y] == BoardStatus.SHIP) {
            board[x][y] = BoardStatus.SHIP_HIT;
            return hit(x, y);
        } else {
            board[x][y] = BoardStatus.FIRED;
            return FireStatus.MISS;
        }
    }

    private void placeAvailable(int x1, int y1, int x2, int y2) throws GamesException {
        for (int i = rangeFrom(x1, x2); i <= rangeTo(x1, x2); i++) {
            for (int j = rangeFrom(y1, y2); j <= rangeTo(y1, y2); j++) {
                if (board[i][j] != BoardStatus.NOTHING) {
                    throw new GamesException(BATTLESHIP_WRONG_PLACEMENT);
                }
            }
        }
    }

    private void inBoard(int x1, int y1, int x2, int y2) throws GamesException {
        inBoard(x1, y1);
        inBoard(x2, y2);
    }

    private void inBoard(int x, int y) throws GamesException {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            throw new GamesException(BATTLESHIP_OFF_THE_BOARD);
        }
    }


    private int rangeFrom(int a1, int a2) {
        return Math.min(a1, a2) == 0 ? 0 : Math.min(a1, a2) - 1;
    }

    private int rangeTo(int a1, int a2) {
        return Math.max(a1, a2) == BOARD_SIZE - 1 ? BOARD_SIZE - 1 : Math.max(a1, a2) + 1;
    }


    private void alreadyFired(int x, int y) throws GamesException {
        if (board[x][y] == BoardStatus.SHIP_HIT || board[x][y] == BoardStatus.FIRED) {
            throw new GamesException(BATTLESHIP_PLACE_FIRED);
        }
    }


    private int hit(int x, int y) {
        int hit = 1;

        for (int i = x - 1; i >= 0; i--) {
            if (board[i][y] == BoardStatus.NOTHING || board[i][y] == BoardStatus.FIRED) {
                break;
            } else if (board[i][y] == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = x + 1; i < BOARD_SIZE; i++) {
            if (board[i][y] == BoardStatus.NOTHING || board[i][y] == BoardStatus.FIRED) {
                break;
            } else if (board[i][y] == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (board[x][i] == BoardStatus.NOTHING || board[x][i] == BoardStatus.FIRED) {
                break;
            } else if (board[x][i] == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = y + 1; i < BOARD_SIZE; i++) {
            if (board[x][i] == BoardStatus.NOTHING || board[x][i] == BoardStatus.FIRED) {
                break;
            } else if (board[x][i] == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        return hit;
    }

}
