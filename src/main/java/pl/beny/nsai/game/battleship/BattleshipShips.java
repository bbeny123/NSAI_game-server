package pl.beny.nsai.game.battleship;

import org.springframework.stereotype.Component;
import pl.beny.nsai.util.GamesException;

import java.util.concurrent.atomic.AtomicInteger;

import static pl.beny.nsai.game.battleship.BattleshipShips.Ships.*;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_SIZE_NOT_AVAILABLE;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_WRONG_SIZE;

@Component
public class BattleshipShips {

    private AtomicInteger size1 = new AtomicInteger(4); //indicates how many 1-size ships left to placed
    private AtomicInteger size2 = new AtomicInteger(3); //indicates how many 2-size ships left to placed
    private AtomicInteger size3 = new AtomicInteger(2); //indicates how many 3-size ships left to placed
    private AtomicInteger size4 = new AtomicInteger(1); //indicates how many 4-size ships left to placed
    private int onBoard;    //indicates how many not sunk ships has player

    //indicates if ships of given size is available to place
    public boolean shipAvailable(int size) throws GamesException {
        return ship(size).get() > 0;
    }

    //decrement ship quantity of given size
    public void placeShip(int size) throws GamesException {
        if (ship(size).getAndDecrement() <= 0) throw new GamesException(BATTLESHIP_SIZE_NOT_AVAILABLE);
        onBoard++;
    }

    //decrement quantity of not sunk ships
    public int destroyShip() {
        return --onBoard;
    }

    //indicates if all ships are already placed
    public boolean allShipsPlaced() {
        return size1.get() <= 0 && size2.get() <= 0 && size3.get() <= 0 && size4.get() <= 0;
    }

    //returns how many ships of given size left to placed
    private AtomicInteger ship(int size) throws GamesException {
        switch (size) {
            case SIZE_1:
                return size1;
            case SIZE_2:
                return size2;
            case SIZE_3:
                return size3;
            case SIZE_4:
                return size4;
            default:
                throw new GamesException(BATTLESHIP_WRONG_SIZE);
        }
    }

    //ship sizes
    public interface Ships {
        int SIZE_1 = 1;
        int SIZE_2 = 2;
        int SIZE_3 = 3;
        int SIZE_4 = 4;
    }
}
