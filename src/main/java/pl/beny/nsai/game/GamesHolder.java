package pl.beny.nsai.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.beny.nsai.game.battleship.Battleship;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GamesHolder extends LinkedHashMap<Long, Game> {

    @Autowired
    private GamesHolder() {
        super(16, 0.75f, true);
    }

    @Override
    public Game get(Object key) {
        Game game = super.get(key);
        game.setLastActivity(LocalDateTime.now());
        return game;
    }

    @Override
    public Game getOrDefault(Object key, Game defaultValue) {
        Game game = super.getOrDefault(key, defaultValue);
        game.setLastActivity(LocalDateTime.now());
        return game;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, Game> eldest) {
        return LocalDateTime.now().minusHours(1).isBefore(eldest.getValue().getLastActivity());
    }

}
