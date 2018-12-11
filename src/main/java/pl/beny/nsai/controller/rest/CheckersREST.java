package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pl.beny.nsai.dto.CheckersRequest;
import pl.beny.nsai.game.GamesHolder;
import pl.beny.nsai.game.checkers.Checkers;
import pl.beny.nsai.game.checkers.CheckersResult;
import pl.beny.nsai.util.GamesException;
import reactor.core.publisher.Flux;
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
    @GetMapping("/dupa")
    public void dupa() {
        try {
            WebClient client = WebClient.create("http://localhost:8080/rest/checkers");
            ParameterizedTypeReference<ServerSentEvent<Object>> type
                    = new ParameterizedTypeReference<ServerSentEvent<Object>>() {};

            Flux<ServerSentEvent<Object>> eventStream = client.get()
                    .uri("/ai/51")
                    .retrieve()
                    .bodyToFlux(type);

            eventStream.subscribe(
                    content -> System.out.println(content.data()),
                    e -> System.out.println(e.getCause().getMessage()),
                    () -> System.out.println("complited"));
        } catch (Exception ex) {
            System.out.println(ex.getCause().getMessage());
        }

    }
}
