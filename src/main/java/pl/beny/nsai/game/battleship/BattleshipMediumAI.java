package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.util.GamesException;
import pl.beny.nsai.util.GamesException.GamesErrors;

import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

//medium AI
public class BattleshipMediumAI {

    private int fireStatus; //last fire status value after calling 'board.fire(x, y)' method

    private int strategy = 0; //strategies to shoot: 0 - down, 1- up, 2 - right, 3 - left

    private boolean underFire;  //if true method fire uses strategies to sunk ship

    private int accurateHitX; //primary successful hit - coordinate x

    private int accurateHitY; //primary successful hit - coordinate y

    private int lastSuccessfulStrategyHitX; //last successful hit from current strategy - coordinate x

    private int lastSuccessfulStrategyHitY; //last successful hit from current strategy - coordinate y

    public BattleshipFireResponse fire(BattleshipBoard board) {
        for (int i = 0; i < 1000; i++) {
            try {
                int x = ThreadLocalRandom.current().nextInt(BattleshipBoard.BOARD_SIZE);
                int y = ThreadLocalRandom.current().nextInt(BattleshipBoard.BOARD_SIZE);
                if (fireStatus > 0) {
                    board.markAsMissAroundSunkShip(lastSuccessfulStrategyHitX, lastSuccessfulStrategyHitY); //after successful sunk mark fields around ship as miss to prevent shooting at this area
                    resetStrategy();
                }
                if (!underFire) {
                    fireStatus = board.fire(x, y);
                    if (BattleshipBoard.FireStatus.HIT == fireStatus) { //if hit was successful assign variables and switch method to strategy mode
                        lastSuccessfulStrategyHitX = x;
                        accurateHitX = x;
                        lastSuccessfulStrategyHitY = y;
                        accurateHitY = y;
                        underFire = true;
                    }
                    return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                } else { //strategy mode
                    if (strategy == 0) {
                        if (accurateHitX < BattleshipBoard.BOARD_SIZE - 1 && lastSuccessfulStrategyHitX < BattleshipBoard.BOARD_SIZE - 1) { //verify boundaries
                            x = lastSuccessfulStrategyHitX + 1;
                            y = lastSuccessfulStrategyHitY;
                            fireStatus = board.fire(x, y);
                            if (BattleshipBoard.FireStatus.HIT == fireStatus) {
                                lastSuccessfulStrategyHitX = x;
                                lastSuccessfulStrategyHitY = y;
                            } else {
                                changeStrategy();
                            }
                            return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                        } else {
                            changeStrategy();
                        }
                    }

                    if (strategy == 1) {
                        if (accurateHitX >= 1 && lastSuccessfulStrategyHitX >= 1) { //verify boundaries
                            x = lastSuccessfulStrategyHitX - 1;
                            y = lastSuccessfulStrategyHitY;
                            fireStatus = board.fire(x, y);
                            if (BattleshipBoard.FireStatus.HIT == fireStatus) {
                                lastSuccessfulStrategyHitX = x;
                                lastSuccessfulStrategyHitY = y;
                            } else {
                                changeStrategy();
                            }
                            return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                        } else {
                            changeStrategy();
                        }
                    }

                    if (strategy == 2) {
                        if (accurateHitY < BattleshipBoard.BOARD_SIZE - 1 && lastSuccessfulStrategyHitY < BattleshipBoard.BOARD_SIZE - 1) { //verify boundaries
                            x = lastSuccessfulStrategyHitX;
                            y = lastSuccessfulStrategyHitY + 1;
                            fireStatus = board.fire(x, y);
                            if (BattleshipBoard.FireStatus.HIT == fireStatus) {
                                lastSuccessfulStrategyHitX = x;
                                lastSuccessfulStrategyHitY = y;
                            } else {
                                changeStrategy();
                            }
                            return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                        } else {
                            changeStrategy();
                        }
                    }

                    if (strategy == 3) {
                        if (accurateHitY >= 1 && lastSuccessfulStrategyHitY >= 1) { //verify boundaries
                            x = lastSuccessfulStrategyHitX;
                            y = lastSuccessfulStrategyHitY - 1;
                            fireStatus = board.fire(x, y);
                            if (BattleshipBoard.FireStatus.HIT == fireStatus) {
                                lastSuccessfulStrategyHitX = x;
                                lastSuccessfulStrategyHitY = y;
                            } else {
                                resetStrategy();
                            }
                            return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                        } else {
                            resetStrategy();
                        }
                    }
                }

            } catch (Exception e) {
                if (e instanceof GamesException) {
                    GamesException gamesException = (GamesException) e;
                    if (GamesErrors.BATTLESHIP_PLACE_FIRED.getCode() == gamesException.getErrorCode()) {
                        if (underFire) {
                            changeStrategy();
                        }
                    }
                }
            }
        }
        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }

    //restores default values
    private void resetStrategy() {
        underFire = false;
        strategy = 0;
    }

    //moves to the next strategy and reassign value of primary successful shoot
    private void changeStrategy() {
        strategy++;
        lastSuccessfulStrategyHitX = accurateHitX;
        lastSuccessfulStrategyHitY = accurateHitY;
    }
}
