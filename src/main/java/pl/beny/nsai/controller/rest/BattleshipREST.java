package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.beny.nsai.dto.battleship.BattleshipFireRequest;
import pl.beny.nsai.dto.battleship.BattleshipPlaceRequest;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.game.battleship.Battleship;
import pl.beny.nsai.game.checkers.Checkers;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/battleship")
public class BattleshipREST extends BaseGameREST<Battleship> {

    @Autowired
    public BattleshipREST(GamesHolder gamesHolder) {
        super(gamesHolder, Battleship.class);
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeShip(@Valid @RequestBody BattleshipPlaceRequest placeRequest) throws Exception {
        placeRequest.isValid();
        return ok(getGame().placeShip(placeRequest));
    }

    @PostMapping("/fire")
    public ResponseEntity<?> fire(@Valid @RequestBody BattleshipFireRequest fireRequest) throws Exception {
        return ok(getGame().fire(fireRequest));
    }

}
