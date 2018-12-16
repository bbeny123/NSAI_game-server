package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.game.battleship.BattleshipShips.Ships;
import pl.beny.nsai.util.GamesException;

import java.util.concurrent.ThreadLocalRandom;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

//random AI
public class BattleshipOloAI {

    //place AI ship on random place
    public static void placeShips(BattleshipShips ships, BattleshipBoard board) throws GamesException {
        for (int i = Ships.SIZE_4; i >= Ships.SIZE_1; i--) {
            for (int j = 0; j < 1000 && ships.shipAvailable(i); j++) {  //fail-safe
                for (int k = 0; k < 10000; k++) {                       //fail-safe
                    try {
                        int xOrY = ThreadLocalRandom.current().nextInt(2);  //ship orientation
                        int x = ThreadLocalRandom.current().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE : BattleshipBoard.BOARD_SIZE + 1 - i);   //ship start coordinates
                        int y = ThreadLocalRandom.current().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE + 1 - i : BattleshipBoard.BOARD_SIZE);   //ship start coordinates
                        board.placeShip(x, y, xOrY == 0 ? x : x + i - 1, xOrY == 0 ? y + i - 1 : y);
                        ships.placeShip(i);
                        break;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    //AI fire on random place
    public static BattleshipFireResponse fire(BattleshipBoard board) {
        for (int i = 0; i < 10000; i++) {    //fail-safe
            try {
                int x = ThreadLocalRandom.current().nextInt(BattleshipBoard.BOARD_SIZE);    //x coordinate
                int y = ThreadLocalRandom.current().nextInt(BattleshipBoard.BOARD_SIZE);    //y coordinate
                return new BattleshipFireResponse(x, y, board.fire(x, y), PLAYER_TURN);
            } catch (Exception e) {
            }
        }
        throw new GamesException(GamesException.GamesErrors.AI_ERROR);
    }
}
