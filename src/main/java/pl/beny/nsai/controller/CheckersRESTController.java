package pl.beny.nsai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.CheckersGamesHolder;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest")
public class CheckersRESTController extends AbstractRESTController {

    private CheckersGamesHolder gamesHolder;
    private Long gameId = 0L;

    @Autowired
    public CheckersRESTController(CheckersGamesHolder gamesHolder) {
        this.gamesHolder = gamesHolder;
    }

    @PostMapping("/checkers/move")
    public ResponseEntity<?> move(@Valid @RequestBody CheckersRequest request) throws Exception {
        return ok(gamesHolder.getGameById(gameId).move(request));
    }

    @GetMapping("/checkers/new")
    public ResponseEntity<?> newGame() {
        gameId++;
        return ok();
    }

}
