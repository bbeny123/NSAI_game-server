package pl.beny.nsai.game.battleship;

import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;

import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class BattleshipBoard {

    public final static int BOARD_SIZE = 10;

    private List<Integer> availableShips = new ArrayList<>();

    public interface BoardStatus {
        int NOTHING = 0;
        int SHIP = 1;
        int SHIP_HIT = 2;
        int FIRED = 3;
    }

    public interface FireStatus {
        int MISS = 0;
        int HIT = -1;
    }

    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    public int board(int x, int y) {
        return board[y][x];
    }

    private void board(int x, int y, int status) {
        board[y][x] = status;
    }

    public void placeShip(int x1, int y1, int x2, int y2) throws GamesException {
        inBoard(x1, y1, x2, y2);
        placeAvailable(x1, y1, x2, y2);
        int size = 0;

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                board(x, y, BoardStatus.SHIP);
                size++;
            }
        }
        availableShips.add(size);
    }

    public int fire(int x, int y) throws GamesException {
        inBoard(x, y);
        alreadyFired(x, y);
        if (board(x, y) == BoardStatus.SHIP) {
            board(x, y, BoardStatus.SHIP_HIT);
            return hit(x, y);
        } else {
            board(x, y, BoardStatus.FIRED);
            return FireStatus.MISS;
        }
    }

    public List<Integer> getAvailableShips() {
        return availableShips;
    }

    public void updateAvailableShipList(int shipSize) {
        availableShips.remove(Integer.valueOf(shipSize));
    }

    public void markAsMissAroundSunkShip(int x1, int y1, int shipSize) {
        boolean vertical = checkIfVertical(x1, y1);
        int x2 = 0;
        int y2 = 0;
        if (shipSize > 1) {
            if (vertical) {
                /*if (board(x1 + 1, y1) == 2) {
                    y2 = y1;
                    x2 = x1 + shipSize - 1;
                } else if (board(x1 - 1, y1) == 2) {
                    y2 = y1;
                    x2 = x1 - shipSize + 1;
                }*/
                if (board(x1, y1 + 1) == 2) {
                    x2 = x1;
                    y2 = y1 + shipSize - 1;
                } else if (board(x1, y1 - 1) == 2) {
                    x2 = x1;
                    y2 = y1 - shipSize + 1;
                }
            } else {
                /*if (board(x1, y1 + 1) == 2) {
                    x2 = x1;
                    y2 = y1 + shipSize - 1;
                } else if (board(x1, y1 - 1) == 2) {
                    x2 = x1;
                    y2 = y1 - shipSize + 1;
                }*/
                if (board(x1 + 1, y1) == 2) {
                    y2 = y1;
                    x2 = x1 + shipSize - 1;
                } else if (board(x1 - 1, y1) == 2) {
                    y2 = y1;
                    x2 = x1 - shipSize + 1;
                }
            }
        } else {
            x2 = x1;
            y2 = y1;
        }

        for (int x = rangeFrom(x1, x2); x <= rangeTo(x1, x2); x++) {
            for (int y = rangeFrom(y1, y2); y <= rangeTo(y1, y2); y++) {
                if (board(x, y) == BoardStatus.NOTHING) {
                    board(x, y, BoardStatus.FIRED);
                }
            }
        }
    }

    private boolean checkIfVertical(int x, int y) {
        if (board(x, y) == board(x, y + 1) && board(x, y + 1) == 2 && (y + 1) < (BOARD_SIZE - 1)) {
            return true;
        }
        if (board(x, y) == board(x, y - 1) && board(x, y - 1) == 2 && (y - 1) >= 0) {
            return true;
        }
        if (board(x, y) == board(x + 1, y) && board(x + 1, y) == 2 && (x + 1) < (BOARD_SIZE - 1)) {
            return false;
        }
        if (board(x, y) == board(x - 1, y) && board(x - 1, y) == 2 && (x - 1) >= 0) {
            return false;
        }
        return false;
    }

    private void placeAvailable(int x1, int y1, int x2, int y2) throws GamesException {
        for (int x = rangeFrom(x1, x2); x <= rangeTo(x1, x2); x++) {
            for (int y = rangeFrom(y1, y2); y <= rangeTo(y1, y2); y++) {
                if (board(x, y) != BoardStatus.NOTHING) {
                    throw new GamesException(BATTLESHIP_WRONG_PLACEMENT);
                }
            }
        }
    }

    public boolean placeAvailableForMap(int criteria, int x, int y, int size, boolean vertical) {
        int z = (vertical ? y : x);
        int end = z + size - 1;
        if (end > BOARD_SIZE - 1) {
            return false;
        }
        int status;
        for (int i = z; i <= end; i++) {
            status = (vertical ? board[x][i] : board[i][y]);
            if (status == criteria) {
                return false;
            }
        }
        return true;
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
        if (board(x, y) == BoardStatus.SHIP_HIT || board(x, y) == BoardStatus.FIRED) {
            throw new GamesException(BATTLESHIP_PLACE_FIRED);
        }
    }


    private int hit(int x, int y) {
        int hit = 1;

        for (int i = x - 1; i >= 0; i--) {
            if (board(i, y) == BoardStatus.NOTHING || board(i, y) == BoardStatus.FIRED) {
                break;
            } else if (board(i, y) == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = x + 1; i < BOARD_SIZE; i++) {
            if (board(i, y) == BoardStatus.NOTHING || board(i, y) == BoardStatus.FIRED) {
                break;
            } else if (board(i, y) == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (board(x, i) == BoardStatus.NOTHING || board(x, i) == BoardStatus.FIRED) {
                break;
            } else if (board(x, i) == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        for (int i = y + 1; i < BOARD_SIZE; i++) {
            if (board(x, i) == BoardStatus.NOTHING || board(x, i) == BoardStatus.FIRED) {
                break;
            } else if (board(x, i) == BoardStatus.SHIP) {
                return FireStatus.HIT;
            } else {
                hit++;
            }
        }

        return hit;
    }

}
