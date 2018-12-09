package pl.beny.nsai.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.beny.nsai.dto.ExceptionResponse;
import pl.beny.nsai.dto.ResponseWrapper;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GamesHolder extends LinkedHashMap<Long, Game> {

    @Autowired
    public GamesHolder() {
        super(16, 0.75f, true);
    }

    @Override
    public Game get(Object key) {
        Game game = super.get(key);
        if (game != null) {
            game.updateLastActivity();
        }
        return game;
    }

    @Override
    public Game getOrDefault(Object key, Game defaultValue) {
        Game game = super.getOrDefault(key, defaultValue);
        if (game != null) {
            game.updateLastActivity();
        }
        return game;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, Game> eldest) {
        return LocalDateTime.now().minusHours(1).isAfter(eldest.getValue().lastActivity());
    }

    @Async
    public void newGameAsync(Game game) {
        if (game != null) {
            game.newGameAsync();
        }
    }

    @Async
    public void moveAI(FluxSink<ResponseWrapper> sink, Long userId, Game game) {
        if (game != null) {
            try {
                game.moveAI().forEach(r -> sink.next(new ResponseWrapper(userId, r)));
            } catch (Exception ex) {
                sink.next(new ResponseWrapper(userId, new ExceptionResponse(ex)));
            }
        }
    }

}
