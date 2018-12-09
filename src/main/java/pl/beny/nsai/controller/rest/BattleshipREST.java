package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.beny.nsai.dto.battleship.BattleshipFireRequest;
import pl.beny.nsai.dto.battleship.BattleshipFireResponse;
import pl.beny.nsai.dto.battleship.BattleshipPlaceRequest;
import pl.beny.nsai.dto.battleship.BattleshipStatusResponse;
import pl.beny.nsai.game.Game;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.game.battleship.Battleship;
import pl.beny.nsai.util.GamesException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/battleship")
public class BattleshipREST extends BaseGameREST<Battleship> {

    @Autowired
    public BattleshipREST(GamesHolder gamesHolder) {
        super(gamesHolder, Battleship.class);
    }

    @PostMapping("/place")
    public Mono<BattleshipStatusResponse> placeShip(@Valid @RequestBody BattleshipPlaceRequest placeRequest) throws Exception {
        placeRequest.isValid();
        return Mono.just(getGame().placeShip(placeRequest));
    }

    @PostMapping("/fire")
    public Mono<BattleshipFireResponse> fire(@Valid @RequestBody BattleshipFireRequest fireRequest) throws GamesException {
        Game game = getGame();
        BattleshipFireResponse response = getGame().fire(fireRequest);
        moveAi(game);
        return Mono.just(response);
    }

}
