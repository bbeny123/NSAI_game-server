package pl.beny.nsai.game;

import org.springframework.stereotype.Component;
import pl.beny.nsai.game.checkers.Checkers;

import java.util.HashMap;

@Component
public class CheckersGamesHolder {

    private HashMap<Long, Checkers> games = new HashMap<>();

    public Checkers getGameById(Long id) {
        if (!games.containsKey(id)) {
            games.put(id, new Checkers());
        }
        return games.get(id);
    }

}
