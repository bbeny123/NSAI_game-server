package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

//HardAI
public class BattleshipHardAI {

    private int fireStatus;                     //response from fire method

    private int[][] probabilities;              //current probability map

    private List<BattleshipProbabilityMap.Coordinate> hits = new ArrayList<>();     //list of previous hits

    private List<Coordinate> bestCoords = new ArrayList<>();                        //list of best coords for next shot

    public BattleshipFireResponse fire(BattleshipBoard board) {
        for (int i = 0; i < 1000; i++) {
            BattleshipProbabilityMap probabilityMap = new BattleshipProbabilityMap(board);      //creating new instance of probability map (every move needs new one)
            probabilityMap.init();                                                              //probability map initialize
            probabilities = probabilityMap.getProbabilities();                                  //loading current probability map
            findBestPosition(probabilities);                                                    //finding best position for next shoot
            Coordinate coord = null;
            getHits(probabilityMap);                                                            //loading previous hits

            if (bestCoords.size() > 1) {                                                        //if more then 1 best position for next shoot then random pick
                int id = getRandomCoords(bestCoords.size());
                coord = bestCoords.get(id);
            } else {
                coord = bestCoords.get(0);
            }

            fireStatus = board.fire(coord.getX(), coord.getY());
            if (fireStatus > 0) {
                board.updateAvailableShipList(fireStatus);                                      //update available ship list after sunk
                board.markAsMissAroundSunkShip(coord.getX(), coord.getY());                     //update fields around sunk ship (as missed ones)
            }

            return new BattleshipFireResponse(coord.getX(), coord.getY(), fireStatus, PLAYER_TURN);
        } throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    //loading previous hits
    private void getHits(BattleshipProbabilityMap probabilityMap) {
        hits = probabilityMap.getHits();
    }

    //random picker for next shoots
    private int getRandomCoords(int size) {
        return ThreadLocalRandom.current().nextInt(size - 1);
    }

    //finding best next shoot coord based on probability map
    private List<Coordinate> findBestPosition(int[][] probabilities) {
        int bestProb = 0;
        Coordinate bestCoord = null;
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                if (probabilities[x][y] > bestProb) {
                    bestProb = probabilities[x][y];                         //finding highest probability on board
                }
            }
        }

        bestCoords.clear();
        for (int x = 0; x < BattleshipBoard.BOARD_SIZE; x++) {
            for (int y = 0; y < BattleshipBoard.BOARD_SIZE; y++) {
                if (probabilities[y][x] == bestProb) {
                    bestCoord = new Coordinate(x, y);                       //finding coords with highest probability
                    bestCoords.add(bestCoord);
                }
            }
        }
        return bestCoords;
    }

    //class for better control on coordinates
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
    }
}
