package pl.beny.nsai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.beny.nsai.dto.battleship.BattleshipFireRequest;
import pl.beny.nsai.dto.battleship.BattleshipPlaceRequest;
import pl.beny.nsai.game.GamesHolder;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest")
public class BattleshipRESTController extends AbstractRESTController {

    private GamesHolder gamesHolder;
    private Long gameId = 0L;

    @Autowired
    public BattleshipRESTController(GamesHolder gamesHolder) {
        this.gamesHolder = gamesHolder;
    }

    @PostMapping("/battleship/place")
    public ResponseEntity<?> placeShip(@Valid @RequestBody BattleshipPlaceRequest placeRequest) throws Exception {
        placeRequest.isValid();
        return ok(gamesHolder.getGameById(gameId).placeShip(placeRequest));
    }

    @PostMapping("/battleship/fire")
    public ResponseEntity<?> fire(@Valid @RequestBody BattleshipFireRequest fireRequest) throws Exception {
        return ok(gamesHolder.getGameById(gameId).fire(fireRequest));
    }

    @GetMapping("/battleship/new")
    public ResponseEntity<?> newGame() {
        gameId++;
        return ok();
    }

}
