package pl.beny.nsai.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.beny.nsai.dto.GameRequest;
import pl.beny.nsai.dto.ResponseWrapper;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.util.GamesException;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import javax.validation.Valid;
import java.time.Duration;

import static pl.beny.nsai.util.GamesException.GamesErrors.GAME_NOT_FOUND;

public abstract class BaseGameREST<T extends Game> extends BaseREST {

    private final GamesHolder gamesHolder;
    private final Class<T> clz;
    private final FluxProcessor<ResponseWrapper, ResponseWrapper> processor;
    private final FluxSink<ResponseWrapper> sink;

    public BaseGameREST(GamesHolder gamesHolder, Class<T> clz) {
        this.gamesHolder = gamesHolder;
        this.clz = clz;
        this.processor = DirectProcessor.create();
        this.sink = processor.sink();
    }

    //new game for requested user
    @PostMapping("/new")
    public ResponseEntity<?> newGame(@Valid @RequestBody GameRequest request) throws Exception {
        T game = clz.newInstance();
        game.newGame(request.getDifficulty());
        gamesHolder.newGameAsync(game);
        gamesHolder.put(getUserContext().getUserId(), game);
        return ok();
    }

    //AI listener for the user with given {id}
    @GetMapping(path = "/ai/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> aiResponse(@PathVariable("id") Long id) {
        return processor.filter(r -> r.getUserId().equals(id)).delayElements(Duration.ofMillis(300)).map(ResponseWrapper::getResponse);
    }

    //call AI move for given game
    protected void moveAi(Game game) {
        gamesHolder.moveAI(sink, getUserContext().getUserId(), game);
    }

    //get game for requested user
    protected T getGame() {
        Game game = gamesHolder.get(getUserContext().getUserId());
        if (!clz.isInstance(game)) {
            throw new GamesException(GAME_NOT_FOUND);
        }
        return clz.cast(game);
    }

}
