package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.util.GamesException;
import pl.beny.nsai.util.GamesException.GamesErrors;

import java.util.Random;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

public class BattleshipMediumAI {

    private int fireStatus;

    private int strategy = 0;

    private boolean underFire;

    private int accurateHitX;

    private int accurateHitY;

    private int lastSuccessfulStrategyHitX;

    private int lastSuccessfulStrategyHitY;

    public BattleshipFireResponse fire(BattleshipBoard board) {
        for (int i = 0; i < 1000; i++) {
            try {
                int x = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                int y = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                if (fireStatus > 0) {
                    board.markAsMissAroundSunkShip(lastSuccessfulStrategyHitX, lastSuccessfulStrategyHitY);
                    resetStrategy();
                }
                if (!underFire) {
                    fireStatus = board.fire(x, y);
                    if (BattleshipBoard.FireStatus.HIT == fireStatus) {
                        lastSuccessfulStrategyHitX = x;
                        accurateHitX = x;
                        lastSuccessfulStrategyHitY = y;
                        accurateHitY = y;
                        underFire = true;
                    }
                    return new BattleshipFireResponse(x, y, fireStatus, PLAYER_TURN);
                } else {
                    if (strategy == 0) {
                        if (accurateHitX < BattleshipBoard.BOARD_SIZE - 1 && lastSuccessfulStrategyHitX < BattleshipBoard.BOARD_SIZE - 1) {
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
                        if (accurateHitX >= 1 && lastSuccessfulStrategyHitX >= 1) {
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
                        if (accurateHitY < BattleshipBoard.BOARD_SIZE - 1 && lastSuccessfulStrategyHitY < BattleshipBoard.BOARD_SIZE - 1) {
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
                        if (accurateHitY >= 1 && lastSuccessfulStrategyHitY >= 1) {
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

    private void resetStrategy() {
        underFire = false;
        strategy = 0;
    }

    private void changeStrategy() {
        strategy++;
        lastSuccessfulStrategyHitX = accurateHitX;
        lastSuccessfulStrategyHitY = accurateHitY;
    }
}
