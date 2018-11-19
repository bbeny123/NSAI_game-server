package pl.beny.nsai.game.battleship;

import org.springframework.stereotype.Component;
import pl.beny.nsai.util.GamesException;

import java.util.concurrent.atomic.AtomicInteger;

import static pl.beny.nsai.game.battleship.BattleshipShips.Ships.*;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_SIZE_NOT_AVAILABLE;
import static pl.beny.nsai.util.GamesException.GamesErrors.BATTLESHIP_WRONG_SIZE;

@Component
public class BattleshipShips {

    public interface Ships {
        int SIZE_1 = 1;
        int SIZE_2 = 2;
        int SIZE_3 = 3;
        int SIZE_4 = 4;
    }

    private AtomicInteger size1 = new AtomicInteger(4);
    private AtomicInteger size2 = new AtomicInteger(3);
    private AtomicInteger size3 = new AtomicInteger(2);
    private AtomicInteger size4 = new AtomicInteger(1);

    private int onBoard;

    public boolean shipAvailable(int size) throws GamesException {
        return ship(size).get() > 0;
    }

    public void placeShip(int size) throws GamesException {
        if (ship(size).getAndDecrement() <= 0) throw new GamesException(BATTLESHIP_SIZE_NOT_AVAILABLE);
        onBoard++;
    }

    public int destroyShip() {
        return --onBoard;
    }

    public boolean allShipsPlaced(){
        return size1.get() <= 0 && size2.get() <= 0 && size3.get() <= 0 && size4.get() <= 0;
    }

    public boolean allDestroyed() {
        return onBoard == 0;
    }

    private AtomicInteger ship(int size) throws GamesException {
        switch (size) {
            case SIZE_1: return size1;
            case SIZE_2: return size2;
            case SIZE_3: return size3;
            case SIZE_4: return size4;
            default: throw new GamesException(BATTLESHIP_WRONG_SIZE);
        }
    }
}
