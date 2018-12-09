package pl.beny.nsai.game.battleship;

import pl.beny.nsai.dto.battleship.BattleshipFireRequest;
import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn;
import pl.beny.nsai.dto.battleship.BattleshipPlaceRequest;
import pl.beny.nsai.dto.battleship.BattleshipStatusResponse;
import pl.beny.nsai.dto.battleship.BattleshipStatusResponse.BattleshipStatus;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.util.GamesException;

import java.util.Collections;
import java.util.List;

import static pl.beny.nsai.util.GamesException.GamesErrors.*;

public class Battleship extends Game {

    public enum Difficulty {
        OloAI
    }

    private int STATUS = BattleshipStatus.PREPARING;
    private int FIRE_TURN = BattleshipStatus.PREPARING;
    private Difficulty difficulty = Difficulty.OloAI;
    private final BattleshipPlayer player = new BattleshipPlayer();
    private final BattleshipPlayer computer = new BattleshipPlayer();

    @Override
    public void newGameAsync() {
        placeShipsAI();
    }

    @Override
    protected void setDifficulty(String difficultyLevel) {
        this.difficulty = Difficulty.valueOf(difficultyLevel);
    }

    public BattleshipStatusResponse placeShip(BattleshipPlaceRequest placeRequest) throws GamesException {
        if (STATUS != BattleshipStatus.PREPARING)
            throw new GamesException(BATTLESHIP_NOT_PREPARING);
        if (!player.getShips().shipAvailable(placeRequest.size()))
            throw new GamesException(BATTLESHIP_SIZE_NOT_AVAILABLE);

        player.getBoard().placeShip(placeRequest.getX1(), placeRequest.getY1(), placeRequest.getX2(), placeRequest.getY2());
        player.getShips().placeShip(placeRequest.size());

        if (player.getShips().allShipsPlaced() && computer.getShips().allShipsPlaced())
            STATUS = BattleshipStatus.BATTLE;

        return new BattleshipStatusResponse(player.getShips().allShipsPlaced() ? BattleshipStatus.BATTLE : STATUS);
    }

    private void placeShipsAI() throws GamesException {
        if (difficulty == Difficulty.OloAI) {
            BattleshipRandomAI.placeShips(computer.getShips(), computer.getBoard());
        }
        if (player.getShips().allShipsPlaced() && computer.getShips().allShipsPlaced())
            STATUS = BattleshipStatus.BATTLE;
    }

    public BattleshipFireResponse fire(BattleshipFireRequest fireRequest) throws GamesException {
        if (STATUS != BattleshipStatus.BATTLE) throw new GamesException(BATTLESHIP_NOT_BATTLE);

        int result = computer.getBoard().fire(fireRequest.getX(), fireRequest.getY());
        if (result > 0 && computer.getShips().destroyShip() <= 0) {
            STATUS = BattleshipStatus.WIN;
            return new BattleshipFireResponse(BattleshipStatus.WIN);
        }

        FIRE_TURN = BattleshipFireTurn.COMPUTER_TURN;
        return new BattleshipFireResponse(result, FIRE_TURN);
    }

    @Override
    public List<Object> moveAI() {
        BattleshipFireResponse response = BattleshipRandomAI.fire(player.getBoard());
        if (response.getEnemyStatus() > 0 && player.getShips().destroyShip() <= 0) {
            STATUS = BattleshipStatus.DEFEAT;
            return Collections.singletonList(new BattleshipFireResponse(BattleshipStatus.DEFEAT));
        }

        FIRE_TURN = BattleshipFireTurn.PLAYER_TURN;
        return Collections.singletonList(response);
    }

}
