package pl.beny.nsai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.beny.nsai.model.Token;
import pl.beny.nsai.repository.TokenRepository;
import pl.beny.nsai.util.RentalException;

@Service
public class TokenService extends BaseService<Token> {

    private TokenRepository repository;

    @Autowired
    public TokenService(TokenRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Token findByToken(String token) throws RentalException {
        return repository.findByToken(token).orElseThrow(() -> new RentalException(RentalException.RentalErrors.TOKEN_NOT_EXISTS));
    }

}
