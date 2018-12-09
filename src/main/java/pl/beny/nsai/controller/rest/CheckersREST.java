package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.game.checkers.Checkers;
import pl.beny.nsai.game.checkers.CheckersResult;
import pl.beny.nsai.util.GamesException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/checkers")
public class CheckersREST extends BaseGameREST<Checkers> {

    @Autowired
    public CheckersREST(GamesHolder gamesHolder) {
        super(gamesHolder, Checkers.class);
    }

    @PostMapping("/move")
    public Mono<CheckersResult> move(@Valid @RequestBody CheckersRequest request) throws GamesException {
        Checkers game = getGame();
        CheckersResult result = game.move(request);
        moveAi(game);
        return Mono.just(result);
    }

}
