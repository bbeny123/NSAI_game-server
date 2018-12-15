package pl.beny.nsai.game.battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BattleshipProbabilityMap {

    private int skewFactor = 2;
    private int[][] probabilities;
    private int[][] board;
    private List<Coordinate> hits = new ArrayList<>();
    private List<Coordinate> adj = new ArrayList<>();
    private BattleshipBoard battleshipBoard;
    private List<Integer> availableShipSizes = new ArrayList<>();

    public BattleshipProbabilityMap(BattleshipBoard battleshipBoard) {
        this.battleshipBoard = battleshipBoard;
        this.availableShipSizes = battleshipBoard.getAvailableShips();
    }

    public void init() {
        probabilities = new int[BattleshipBoard.BOARD_SIZE][BattleshipBoard.BOARD_SIZE];
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                probabilities[x][y] = 0;
            }
        }
        recalculateProbabilities();
        showProb();
    }

    public int[][] getProbabilities() {
        return probabilities;
    }

    public List<Coordinate> getHits() {
        return hits;
    }

    private void recalculateProbabilities() {
        board = new int[BattleshipBoard.BOARD_SIZE][BattleshipBoard.BOARD_SIZE];
        board = loadBoard();
        // przepisanie aktualnej planszy
        for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                if (board[y][x] == 2) {
                    hits.add(new Coordinate(x, y));
                }
            }
        }

        //wyliczenie prawdopodobieństwa dla każdego typu statku
        int listSize = availableShipSizes.size();
        for (int i = 0; i < listSize; i++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                    // poziomo
                    if (shipCanBePlaced(3, x, y, availableShipSizes.get(i), false)) {
                        increaseProbablity(x, y, availableShipSizes.get(i), false);
                    }
                    //pionowo
                    if (shipCanBePlaced(3, x, y, availableShipSizes.get(i), true)) {
                        increaseProbablity(x, y, availableShipSizes.get(i), false);
                    }
                }
            }
        }

        skewProbabilityAroundHits(hits);

        adaptToCurrentBoard();
    }

    private void adaptToCurrentBoard() {
        for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
            for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
                /*if (board[x][y] == 2 || board[x][y] == 3) {
                    if (probabilities[x][y] != 0) {
                        probabilities[x][y] = 0;
                    }
                }*/
                if (board[y][x] == 2 || board[y][x] == 3) {
                    if (probabilities[y][x] != 0) {
                        probabilities[y][x] = 0;
                    }
                }
            }
        }
    }

    private void skewProbabilityAroundHits(List<Coordinate> hits) {
        List<Coordinate> toSkew = new ArrayList<>();
        Set<Coordinate> skewNoDuplicate = new HashSet<>();
        toSkew = hits;
        List<Coordinate> join = new ArrayList<>(toSkew);
        for (int i = 0; i < hits.size(); i++) {
            List<Coordinate> tmp = getAdjacentPosition(toSkew.get(i));
            join.addAll(tmp);
        }

        for (int k = 0; k < join.size(); k++) {
            skewNoDuplicate.add(join.get(k));
        }

        for (Coordinate coordinate : skewNoDuplicate) {
            // poprawa strzelania po trafie zamina x y
            /*if (!hits.contains(coordinate)) {
                probabilities[coordinate.getX()][coordinate.getY()] *= skewFactor;
            } else {
                probabilities[coordinate.getX()][coordinate.getY()] = 0;
            }*/
            if (!hits.contains(coordinate)) {
                probabilities[coordinate.getY()][coordinate.getX()] *= skewFactor;
            } else {
                probabilities[coordinate.getY()][coordinate.getX()] = 0;
            }
        }
    }

    private List<Coordinate> getAdjacentPosition(Coordinate coordinate) {

        for (Coordinate coord : hits) {
            if (coord.X == coordinate.X && coord.Y != coordinate.Y && (coord.Y - coordinate.Y) < 4) {
                findPossibleShipPlacementsByY(coordinate);
            } else if (coord.Y == coordinate.Y && coord.X != coordinate.X && (coord.X - coordinate.X) < 4) {
                findPossibleShipPlacementsByX(coordinate);
            } else {
                findPossibleShipPlacementsByX(coordinate);
                findPossibleShipPlacementsByY(coordinate);
            }
        }
        return adj;
    }

    private void findPossibleShipPlacementsByY(Coordinate coordinate) {
        if (coordinate.getY() + 1 < BattleshipBoard.BOARD_SIZE) {
            adj.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        }
        if (coordinate.getY() - 1 >= 0) {
            adj.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        }
    }

    private void findPossibleShipPlacementsByX(Coordinate coordinate) {
        if (coordinate.getX() + 1 < BattleshipBoard.BOARD_SIZE) {
            adj.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        }
        if (coordinate.getX() - 1 >= 0) {
            adj.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        }
    }

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

    private boolean shipCanBePlaced(int criteriaForRejection, int x, int y, int shipSize, boolean vertical) {
        int x1 = x + shipSize - 1;
        int y1 = y + shipSize - 1;

        if (x1 > BattleshipBoard.BOARD_SIZE - 1 || y1 > BattleshipBoard.BOARD_SIZE - 1) {
            return false;
        }

        // poziomo
        if (!vertical) {
            return battleshipBoard.placeAvailableForMap(criteriaForRejection, x, y, shipSize, false);
        } else if (vertical) {
            return battleshipBoard.placeAvailableForMap(criteriaForRejection, x, y, shipSize, true);
        }
        return true;
    }

    private int[][] loadBoard() {
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                board[y][x] = battleshipBoard.board(x, y);
            }
        }
        return board;
    }

    private void showProb() {
        for (int i = 0; i < BattleshipBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < BattleshipBoard.BOARD_SIZE; j++) {
                System.out.print(probabilities[j][i] + "\t");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        System.out.print("\n");
    }

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
