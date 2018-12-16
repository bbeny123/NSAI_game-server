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

//main Battleship class
public class Battleship extends Game {

    //available game difficulties
    public enum Difficulty {
        OloAI,
        MediumAI,
        HardAI
    }

    private int STATUS = BattleshipStatus.PREPARING;                        //current game status (values: pl.beny.nsai.dto.battleship.BattleshipStatusResponse.BattleshipStatus)
    private int FIRE_TURN = BattleshipFireTurn.PLAYER_TURN;                 //indicates whose turn it is (values: pl.beny.nsai.dto.battleship.BattleshipFireResponse.BattleshipFireTurn)
    private Difficulty difficulty = Difficulty.OloAI;                       //game difficulty (values: Difficulty)
    private final BattleshipPlayer player = new BattleshipPlayer();         //player data
    private final BattleshipPlayer computer = new BattleshipPlayer();       //AI data
    private final BattleshipMediumAI mediumAI = new BattleshipMediumAI();   //instance of MediumAI
    private final BattleshipHardAI hardAI = new BattleshipHardAI();         //instance of HardAI

    //place all AI ships
    @Override
    public void newGameAsync() {
        placeShipsAI();
    }

    //set difficulty
    @Override
    protected void setDifficulty(String difficultyLevel) {
        this.difficulty = Difficulty.valueOf(difficultyLevel);
    }

    //validates and executes player placeRequest
    public BattleshipStatusResponse placeShip(BattleshipPlaceRequest placeRequest) throws GamesException {
        if (STATUS != BattleshipStatus.PREPARING)                       //check if game is in PREPARING phase
            throw new GamesException(BATTLESHIP_NOT_PREPARING);
        if (!player.getShips().shipAvailable(placeRequest.size()))      //check if player has unplaced ship with requested size
            throw new GamesException(BATTLESHIP_SIZE_NOT_AVAILABLE);

        player.getBoard().placeShip(placeRequest.getX1(), placeRequest.getY1(), placeRequest.getX2(), placeRequest.getY2());    //places ship on the board
        player.getShips().placeShip(placeRequest.size());                                                                       //update player data

        if (player.getShips().allShipsPlaced() && computer.getShips().allShipsPlaced())                                         //check if all player and AI ships are placed
            STATUS = BattleshipStatus.BATTLE;                                                                                   //if yes then game phase is changed to BATTLE phase

        return new BattleshipStatusResponse(player.getShips().allShipsPlaced() ? BattleshipStatus.BATTLE : STATUS);
    }

    //places all AI ships
    private void placeShipsAI() throws GamesException {
        BattleshipOloAI.placeShips(computer.getShips(), computer.getBoard());

        if (player.getShips().allShipsPlaced() && computer.getShips().allShipsPlaced())     //check if all player and AI ships are placed
            STATUS = BattleshipStatus.BATTLE;                                               //if yes then game phase is changed to BATTLE phase
    }

    //validates and executes player fireRequest
    public BattleshipFireResponse fire(BattleshipFireRequest fireRequest) throws GamesException {
        if (STATUS != BattleshipStatus.BATTLE) throw new GamesException(BATTLESHIP_NOT_BATTLE); //check if game is in BATTLE phase

        int result = computer.getBoard().fire(fireRequest.getX(), fireRequest.getY());          //get result of fireRequest (values: pl.beny.nsai.game.battleship.BattleshipBoard.BoardStatus)
        if (result > 0 && computer.getShips().destroyShip() <= 0) {                             //check if result indicates that some ship was sunk and AI has no more ships
            STATUS = BattleshipStatus.WIN;                                                      //if yes then game status is changed to WIN
            return new BattleshipFireResponse(BattleshipStatus.WIN);
        }

        FIRE_TURN = BattleshipFireTurn.COMPUTER_TURN;                                           //changes turn to COMPUTER_TURN
        return new BattleshipFireResponse(result, FIRE_TURN);
    }

    //AI fire (dependent on difficulty)
    @Override
    public List<Object> moveAI() {
        BattleshipFireResponse response = null;

        if (difficulty == Difficulty.OloAI) {
            response = BattleshipOloAI.fire(player.getBoard());
        }
        if (difficulty == Difficulty.MediumAI) {
            response = mediumAI.fire(player.getBoard());
        }
        if (difficulty == Difficulty.HardAI) {
            response = hardAI.fire(player.getBoard());
        }

        if (response.getEnemyStatus() > 0 && player.getShips().destroyShip() <= 0) {                //check if result indicates that some ship was sunk and player has no more ships
            STATUS = BattleshipStatus.DEFEAT;                                                       //if yes then game status is changed to DEFEAT
            return Collections.singletonList(new BattleshipFireResponse(BattleshipStatus.DEFEAT));
        }

        FIRE_TURN = BattleshipFireTurn.PLAYER_TURN;                                                 //changes turn to PLAYER_TURN
        return Collections.singletonList(response);
    }

}
