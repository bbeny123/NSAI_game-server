package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

public class BattleshipHardAI {

    private int fireStatus;

    private int accurateHitX;

    private int accurateHitY;

    private int[][] probabilities;

    private List<BattleshipProbabilityMap.Coordinate> hits = new ArrayList<>();

    private List<Coordinate> bestCoords = new ArrayList<>();

    public BattleshipFireResponse fire(BattleshipBoard board) {
        for (int i = 0; i < 1000; i++) {
            BattleshipProbabilityMap probabilityMap = new BattleshipProbabilityMap(board);
            probabilityMap.init();
            probabilities = probabilityMap.getProbabilities();
            findBestPosition(probabilities);
            Coordinate coord = null;
            getHits(probabilityMap);
            removeHitsFromShootPossible();
            if (bestCoords.size() > 1) {
                int id = getRandomCoords(bestCoords.size());
                coord = bestCoords.get(id);
            } else {
                coord = bestCoords.get(0);
            }

            fireStatus = board.fire(coord.getX(), coord.getY());
            if (fireStatus > 0) {
                board.updateAvailableShipList(fireStatus);
            }
            return new BattleshipFireResponse(coord.getX(), coord.getY(), fireStatus, PLAYER_TURN);
        } throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    private void removeHitsFromShootPossible() {
        for (Coordinate coordinate : bestCoords) {
            if (hits.contains(coordinate)) {
                bestCoords.remove(coordinate);
            }
        }
    }

    private void getHits(BattleshipProbabilityMap probabilityMap) {
        hits = probabilityMap.getHits();
    }

    private int getRandomCoords(int size) {
        return new Random().nextInt(size - 1);
    }

    private List<Coordinate> findBestPosition(int[][] probabilities) {
        int bestProb = 0;
        Coordinate bestCoord = null;
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                if (probabilities[x][y] > bestProb) {
                    bestProb = probabilities[x][y];
                }
            }
        }

        bestCoords.clear();
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                if (probabilities[y][x] == bestProb) {
                    bestCoord = new Coordinate(x, y);
                    bestCoords.add(bestCoord);
                }
            }
        }
        return bestCoords;
    }

    class Coordinate {
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

        @Override
        public String toString() {
            return "Coordinate{" +
                    "X=" + X +
                    ", Y=" + Y +
                    '}';
        }
    }
}
