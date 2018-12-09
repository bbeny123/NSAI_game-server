package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.AbstractBattleshipResponse.BattleshipStatus;
import pl.beny.nsai.dto.battleship.BattleshipFireRequest;
import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.dto.battleship.BattleshipPlaceRequest;
import pl.beny.nsai.dto.battleship.BattleshipPlacedResponse;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.util.GamesException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class Battleship extends Game {

    public enum Difficulty {
        OloAI
    }

    private Difficulty difficulty = Difficulty.OloAI;
    private int STATUS = BattleshipStatus.PREPARING;

    private BattleshipPlayer player = new BattleshipPlayer();
    private BattleshipPlayer computer = new BattleshipPlayer();

    public BattleshipPlacedResponse placeShip(BattleshipPlaceRequest placeRequest) throws GamesException {
        if (STATUS != BattleshipStatus.PREPARING) throw new GamesException(BATTLESHIP_NOT_PREPARING);
        if (!player.getShips().shipAvailable(placeRequest.size()))
            throw new GamesException(BATTLESHIP_SIZE_NOT_AVAILABLE);

        player.getBoard().placeShip(placeRequest.getX1(), placeRequest.getY1(), placeRequest.getX2(), placeRequest.getY2());
        player.getShips().placeShip(placeRequest.size());
        BattleshipRandomAI.placeShip(computer.getShips(), computer.getBoard());

        if (player.getShips().allShipsPlaced() && computer.getShips().allShipsPlaced())
            STATUS = BattleshipStatus.BATTLE;

        return new BattleshipPlacedResponse(STATUS);
    }

    public BattleshipFireResponse fire(BattleshipFireRequest fireRequest) throws GamesException {
        if (STATUS != BattleshipStatus.BATTLE) throw new GamesException(BATTLESHIP_NOT_BATTLE);

        int result = computer.getBoard().fire(fireRequest.getX(), fireRequest.getY());
        if (result > 0 && computer.getShips().destroyShip() <= 0) {
            STATUS = BattleshipStatus.WIN;
            return new BattleshipFireResponse(BattleshipStatus.WIN);
        }

        BattleshipFireResponse response = BattleshipRandomAI.fire(player.getBoard());
        if (response.getEnemyStatus() > 0 && player.getShips().destroyShip() <= 0) {
            STATUS = BattleshipStatus.DEFEAT;
            return new BattleshipFireResponse(BattleshipStatus.DEFEAT);
        }

        response.setPlayerStatus(result);
        return response;
    }

    @Override
    public void setDifficulty(String difficultyLevel) {
        this.difficulty = Difficulty.valueOf(difficultyLevel);
    }
}
