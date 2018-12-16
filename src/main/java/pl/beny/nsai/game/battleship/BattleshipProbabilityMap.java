package pl.beny.nsai.game.battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//instance of probability table (1 per player and 1 move)
public class BattleshipProbabilityMap {

    private int skewFactor = 2;                                                 //factor of multiplying probability after hit around this coordinates
    private int[][] probabilities;                                              //table of probabilities for every coordinate
    private int[][] board;                                                      //current game board status
    private List<Coordinate> hits = new ArrayList<>();                          //list of all hits
    private List<Coordinate> adj = new ArrayList<>();                           //list of probable next places of ship
    private BattleshipBoard battleshipBoard;                                    //current battleship board instance
    private List<Integer> availableShipSizes = new ArrayList<>();               //list of not sunk ships

    public BattleshipProbabilityMap(BattleshipBoard battleshipBoard) {
        this.battleshipBoard = battleshipBoard;
        this.availableShipSizes = battleshipBoard.getAvailableShips();
    }

    //initialize of generating new probablity map
    public void init() {
        probabilities = new int[BattleshipBoard.BOARD_SIZE][BattleshipBoard.BOARD_SIZE];
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                probabilities[x][y] = 0;
            }
        }
        recalculateProbabilities();
    }

    //array of probabilities
    public int[][] getProbabilities() {
        return probabilities;
    }

    //list of all hits
    public List<Coordinate> getHits() {
        return hits;
    }

    //recalculation of probability map based on board status
    private void recalculateProbabilities() {
        board = new int[BattleshipBoard.BOARD_SIZE][BattleshipBoard.BOARD_SIZE];
        board = loadBoard();                                                            //load of current game board status

        // rewrite all hits from board to hit list
        for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                if (board[y][x] == 2) {
                    hits.add(new Coordinate(x, y));
                }
            }
        }

        //calculation of probability for every coordinate
        int listSize = availableShipSizes.size();
        for (int i = 0; i < listSize; i++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                    // horizontal
                    if (shipCanBePlaced(3, x, y, availableShipSizes.get(i), false)) {       //if ship part can be in coords increase probability
                        increaseProbablity(x, y, availableShipSizes.get(i), false);                         //probability increase
                    }
                    // vertical
                    if (shipCanBePlaced(3, x, y, availableShipSizes.get(i), true)) {        //if ship part can be in coords increase probability
                        increaseProbablity(x, y, availableShipSizes.get(i), false);                         //probability increase
                    }
                }
            }
        }

        skewProbabilityAroundHits(hits);                        //increasing probability around previous hits

        adaptToCurrentBoard();                                  //change probability to 0 on coords where was already shot
    }

    //change probability to 0 on coords where was already shot
    private void adaptToCurrentBoard() {
        for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                if (board[y][x] == 2 || board[y][x] == 3) {
                    if (probabilities[y][x] != 0) {
                        probabilities[y][x] = 0;
                    }
                }
            }
        }
    }

    //increasing probability around previous hits
    private void skewProbabilityAroundHits(List<Coordinate> hits) {
        List<Coordinate> toSkew = new ArrayList<>();
        Set<Coordinate> skewNoDuplicate = new HashSet<>();
        toSkew = hits;
        List<Coordinate> join = new ArrayList<>(toSkew);
        for (int i = 0; i < hits.size(); i++) {
            List<Coordinate> tmp = getAdjacentPosition(toSkew.get(i));          //finding coords up/down/left/right to previous hit
            join.addAll(tmp);
        }

        for (int k = 0; k < join.size(); k++) {
            skewNoDuplicate.add(join.get(k));                                   //removing possible duplicates of possible shots
        }

        for (Coordinate coordinate : skewNoDuplicate) {                         //multiplying posibility around previous shot and reset prob for previous hit to 0

            if (!hits.contains(coordinate)) {
                probabilities[coordinate.getY()][coordinate.getX()] *= skewFactor;
            } else {
                probabilities[coordinate.getY()][coordinate.getX()] = 0;
            }
        }
    }

    //finding best next shot around previous hit based on max ship size
    private List<Coordinate> getAdjacentPosition(Coordinate coordinate) {

        for (Coordinate coord : hits) {
            if (coord.X == coordinate.X && coord.Y != coordinate.Y && (coord.Y - coordinate.Y) <= 4) {
                findPossibleShipPlacementsByY(coordinate);
            } else if (coord.Y == coordinate.Y && coord.X != coordinate.X && (coord.X - coordinate.X) <= 4) {
                findPossibleShipPlacementsByX(coordinate);
            } else {
                findPossibleShipPlacementsByX(coordinate);
                findPossibleShipPlacementsByY(coordinate);
            }
        }
        return adj;
    }

    //finding best next Y coordinate around previous hit
    private void findPossibleShipPlacementsByY(Coordinate coordinate) {
        if (coordinate.getY() + 1 < BattleshipBoard.BOARD_SIZE) {
            adj.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        }
        if (coordinate.getY() - 1 >= 0) {
            adj.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        }
    }

    //finding best next X coordinate around previous hit
    private void findPossibleShipPlacementsByX(Coordinate coordinate) {
        if (coordinate.getX() + 1 < BattleshipBoard.BOARD_SIZE) {
            adj.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        }
        if (coordinate.getX() - 1 >= 0) {
            adj.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        }
    }

    //increasing probability
    private void increaseProbablity(int x, int y, int shipSize, boolean vertical) {
        int z = (vertical ? y : x);
        int end = z + shipSize - 1;

        for (int i = z; i <= end; i++) {
            if (vertical) {
                probabilities[x][i]++;
            } else {
                probabilities[i][y]++;
            }
        }
    }

    //checking if ship can be placed to increase probability
    private boolean shipCanBePlaced(int criteriaForRejection, int x, int y, int shipSize, boolean vertical) {
        int x1 = x + shipSize - 1;
        int y1 = y + shipSize - 1;

        if (x1 > BattleshipBoard.BOARD_SIZE - 1 || y1 > BattleshipBoard.BOARD_SIZE - 1) {
            return false;
        }

        if (!vertical) {
            return battleshipBoard.placeAvailableForMap(criteriaForRejection, x, y, shipSize, false);
        } else if (vertical) {
            return battleshipBoard.placeAvailableForMap(criteriaForRejection, x, y, shipSize, true);
        }
        return true;
    }

    //load current game board before move
    private int[][] loadBoard() {
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                board[y][x] = battleshipBoard.board(x, y);
            }
        }
        return board;
    }

    //class for better control on coordinates
    static class Coordinate {
        public int X;
        public int Y;

        public Coordinate(int x, int y) {
            X = x;
            Y = y;
        }

        public int getX() {
            return X;
        }

        public int getY() {
            return Y;
        }
    }
}
