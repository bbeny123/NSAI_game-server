package pl.beny.nsai.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.beny.nsai.dto.GameRequest;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.util.GamesException;

import javax.validation.Valid;

import static pl.beny.nsai.util.GamesException.GamesErrors.GAME_NOT_FOUND;

public abstract class BaseGameREST<T extends Game> extends BaseREST {

    private final GamesHolder gamesHolder;
    private final Class<T> clz;

    public BaseGameREST(GamesHolder gamesHolder, Class<T> clz) {
        this.gamesHolder = gamesHolder;
        this.clz = clz;
    }

    @PostMapping("/new")
    public ResponseEntity<?> newGame(@Valid @RequestBody GameRequest request) throws Exception {
        T game = clz.newInstance();
        game.setDifficultyLevel(request.getDifficulty());
        gamesHolder.put(getUserContext().getUserId(), game);
        return ok();
    }

    protected T getGame() {
        Game game = gamesHolder.get(getUserContext().getUserId());
        if (clz.isInstance(game)) {
            throw new GamesException(GAME_NOT_FOUND);
        }
        return clz.cast(game);
    }

}
