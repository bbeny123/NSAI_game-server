package pl.beny.nsai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.beny.nsai.model.Token;
import pl.beny.nsai.repository.TokenRepository;
import pl.beny.nsai.util.GamesException;

@Service
public class TokenService extends BaseService<Token, TokenRepository> {

    @Autowired
    public TokenService(TokenRepository repository) {
        super(repository);
    }

    public Token findByToken(String token) throws GamesException {
        return getRepository().findByToken(token).orElseThrow(() -> new GamesException(GamesException.GamesErrors.TOKEN_NOT_EXISTS));
    }

}
