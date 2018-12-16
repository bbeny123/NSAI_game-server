package pl.beny.nsai.game.battleship;

import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;

import static pl.beny.nsai.util.GamesException.GamesErrors.*;

//instance of player/AI board (1 per player)
public class BattleshipBoard {

    public final static int BOARD_SIZE = 10;

    //list of ships on the board
    private List<Integer> availableShips = new ArrayList<>();

    //possible values of the board
    public interface BoardStatus {
        int NOTHING = 0;
        int SHIP = 1;
        int SHIP_HIT = 2;
        int FIRED = 3;
    }

    //possible results of fire move (FireStatus == x and x > 0 indicates that ship of size x was sunk
    public interface FireStatus {
        int MISS = 0;
        int HIT = -1;
    }

    //array of BoardStatus
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    //get BoardStatus for given x,y
    public int board(int x, int y) {
        return board[y][x];
    }

    //set BoardStatus for given x,y
    private void board(int x, int y, int status) {
        board[y][x] = status;
    }

    //place ship for given start and end coordinates
    public void placeShip(int x1, int y1, int x2, int y2) throws GamesException {
        inBoard(x1, y1, x2, y2);                //check if given coordinates are in the board
        placeAvailable(x1, y1, x2, y2);         //check if in given place ship can be placed
        int size = 0;

        //placing ship from start to end coordinates
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                board(x, y, BoardStatus.SHIP);
                size++;
            }
        }
        availableShips.add(size);
    }

    //fire on given coordinates
    public int fire(int x, int y) throws GamesException {
        inBoard(x, y);                                  //check if given coordinates are in the board
        alreadyFired(x, y);                             //check if given coordinates was fired before
        if (board(x, y) == BoardStatus.SHIP) {          //check if on give coordinates ship is placed
            board(x, y, BoardStatus.SHIP_HIT);          //if yes set on give coordinates BoardStatus.SHIP_HIT
            return hit(x, y);                           //check if hit = sunk and return result
        } else {
            board(x, y, BoardStatus.FIRED);             //if not set on give coordinates BoardStatus.FIRED
            return FireStatus.MISS;                     //return result FireStatus.MISS
        }
    }

    //returns not sunk ships from the board
    public List<Integer> getAvailableShips() {
        return availableShips;
    }

    //removes ship after sunk
    public void updateAvailableShipList(int shipSize) {
        availableShips.remove(Integer.valueOf(shipSize));
    }

    //marks space around sunk ship as miss for efficient algorithm working
    public void markAsMissAroundSunkShip(int x, int y) {

        int x1 = x, x2 = x, y1 = y, y2 = y;

        for (int i = x - 1; i >= 0; i--) {
            if (board(i, y) == BoardStatus.NOTHING || board(i, y) == BoardStatus.FIRED) {
                break;
            } else {
                x1--;
            }
        }

        for (int i = x + 1; i < BOARD_SIZE; i++) {
            if (board(i, y) == BoardStatus.NOTHING || board(i, y) == BoardStatus.FIRED) {
                break;
            } else {
                x2++;
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (board(x, i) == BoardStatus.NOTHING || board(x, i) == BoardStatus.FIRED) {
                break;
            } else {
                y1--;
            }
        }

        for (int i = y + 1; i < BOARD_SIZE; i++) {
            if (board(x, i) == BoardStatus.NOTHING || board(x, i) == BoardStatus.FIRED) {
                break;
            } else {
                y2++;
            }
        }

        for (int i = rangeFrom(x1, x2); i <= rangeTo(x1, x2); i++) {
            for (int j = rangeFrom(y1, y2); j <= rangeTo(y1, y2); j++) {
                if (board(i, j) == BoardStatus.NOTHING) {
                    board(i, j, BoardStatus.FIRED);
                }
            }
        }
    }

    //checks if board from start to end coordinates and the board around them are empty
    private void placeAvailable(int x1, int y1, int x2, int y2) throws GamesException {
        for (int x = rangeFrom(x1, x2); x <= rangeTo(x1, x2); x++) {
            for (int y = rangeFrom(y1, y2); y <= rangeTo(y1, y2); y++) {
                if (board(x, y) != BoardStatus.NOTHING) {
                    throw new GamesException(BATTLESHIP_WRONG_PLACEMENT);
                }
            }
        }
    }

    //checks if board from start to end coordinates can place ship only for probability algorithm
    public boolean placeAvailableForMap(int criteria, int x, int y, int size, boolean vertical) {
        int z = (vertical ? y : x);                             //in case of vertical value proper x or y changes
        int end = z + size - 1;
        if (end > BOARD_SIZE - 1) {
            return false;
        }
        int status;
        for (int i = z; i <= end; i++) {
            status = (vertical ? board[x][i] : board[i][y]);    //in case of vertical value proper board coordinates
            if (status == criteria) {
                return false;
            }
        }
        return true;
    }

    //check if given coordinates are in the board
    private void inBoard(int x1, int y1, int x2, int y2) throws GamesException {
        inBoard(x1, y1);
        inBoard(x2, y2);
    }

    //check if given coordinates are in the board
    private void inBoard(int x, int y) throws GamesException {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            throw new GamesException(BATTLESHIP_OFF_THE_BOARD);
        }
    }

    //returns smaller coordinate - 1 if is in the board else returns smaller coordinates
    private int rangeFrom(int a1, int a2) {
        return Math.min(a1, a2) == 0 ? 0 : Math.min(a1, a2) - 1;
    }

    //returns greater coordinate + 1 if is in the board else returns greater coordinates
    private int rangeTo(int a1, int a2) {
        return Math.max(a1, a2) == BOARD_SIZE - 1 ? BOARD_SIZE - 1 : Math.max(a1, a2) + 1;
    }

    //checks if given coordinates was fired before
    private void alreadyFired(int x, int y) throws GamesException {
        if (board(x, y) == BoardStatus.SHIP_HIT || board(x, y) == BoardStatus.FIRED) {
            throw new GamesException(BATTLESHIP_PLACE_FIRED);
        }
    }

    //checks if hit = sunk and returns size of sunk ship or FireStatus.HIT
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
