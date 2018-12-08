package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.game.checkers.Checkers;
import pl.beny.nsai.util.GamesException;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/checkers")
public class CheckersREST extends BaseGameREST<Checkers> {

    @Autowired
    public CheckersREST(GamesHolder gamesHolder) {
        super(gamesHolder, Checkers.class);
    }

    @PostMapping("/move")
    public ResponseEntity<?> move(@Valid @RequestBody CheckersRequest request) throws GamesException {
        return ok(getGame().move(request));
    }

    @PostMapping("/moveAI")
    public ResponseEntity<?> moveAI() throws GamesException {
        return ok(getGame().moveAI());
    }

}
