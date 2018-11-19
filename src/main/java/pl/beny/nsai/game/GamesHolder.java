package pl.beny.nsai.game;

import org.springframework.stereotype.Component;
import pl.beny.nsai.game.battleship.Battleship;

import java.util.HashMap;

@Component
public class GamesHolder {

    private HashMap<Long, Battleship> games = new HashMap<>();

    public Battleship getGameById(Long id) {
        if (!games.containsKey(id)) {
            games.put(id, new Battleship());
        }
        return games.get(id);
    }

}
