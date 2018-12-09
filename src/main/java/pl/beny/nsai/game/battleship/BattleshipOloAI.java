package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.game.battleship.BattleshipShips.Ships;
import pl.beny.nsai.util.GamesException;

import java.util.Random;

import static pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn.PLAYER_TURN;

public class BattleshipOloAI {

    public static void placeShips(BattleshipShips ships, BattleshipBoard board) throws GamesException {
        for (int i = Ships.SIZE_4; i >= Ships.SIZE_1; i--) {
            while (ships.shipAvailable(i)) {
                while (true) {
                    try {
                        int xOrY = new Random().nextInt(2);
                        int x = new Random().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE : BattleshipBoard.BOARD_SIZE + 1 - i);
                        int y = new Random().nextInt(xOrY == 0 ? BattleshipBoard.BOARD_SIZE + 1 - i : BattleshipBoard.BOARD_SIZE);
                        board.placeShip(x, y, xOrY == 0 ? x : x + i - 1, xOrY == 0 ? y + i - 1 : y);
                        ships.placeShip(i);
                        break;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public static BattleshipFireResponse fire(BattleshipBoard board) {
        while (true) {
            try {
                int x = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                int y = new Random().nextInt(BattleshipBoard.BOARD_SIZE);
                return new BattleshipFireResponse(x, y, board.fire(x, y), PLAYER_TURN);
            } catch (Exception e) {
            }
        }
    }
}
