package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.BattleshipFireResponse;
import pl.beny.nsai.game.battleship.BattleshipShips.Ships;
import pl.beny.nsai.util.GamesException;
import pl.beny.nsai.util.GamesException.GamesErrors;

import java.util.Random;

public class BattleshipRandomAI {

    public static void placeShip(BattleshipShips ships, BattleshipBoard board) throws GamesException {
        for(int i = Ships.SIZE_4; i >= Ships.SIZE_1; i--) {
            if (ships.shipAvailable(i)) {
                while (true) {
                    try {
                        int xOrY = new Random().nextInt(2);
                        int x = new Random().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE : BattleshipBoard.BOARD_SIZE + 1 - i);
                        int y = new Random().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE + 1 - i : BattleshipBoard.BOARD_SIZE);
                        board.placeShip(x, y, xOrY == 0 ? x : x + i, xOrY == 0 ? y + 1 : y);
                        ships.placeShip(i);
                        return;
                    } catch (Exception e) { }
                }
            }
        }
        throw new GamesException(GamesErrors.AI_ERROR);
    }

    public static BattleshipFireResponse fire(BattleshipBoard board) {
        while (true) {
            try {
                int x = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                int y = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                return new BattleshipFireResponse(x, y, board.fire(x, y));
            } catch (Exception e) {
            }
        }
    }

}
