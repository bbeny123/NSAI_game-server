package pl.beny.nsai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.beny.nsai.model.Token;
import pl.beny.nsai.model.User;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.repository.UserRepository;
import pl.beny.nsai.util.GamesException;
import pl.beny.nsai.util.MailUtil;

import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserRepository> implements UserDetailsService {

    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder) {
        super(repository);
        this.encoder = encoder;
    }

    @Override
    public UserContext loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserContext(getRepository().findOneByEmail(email).orElseThrow(() -> new UsernameNotFoundException("The e-mail does not exist in database")));
    }

    public boolean existsByEmail(String email) {
        return getRepository().existsByEmail(email);
    }

    public void create(User user) throws GamesException {
        if (existsByEmail(user.getEmail())) throw new GamesException(GamesException.GamesErrors.USER_EXISTS);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setType(User.Type.U);
        user.setActive(false);
        user.setToken(UUID.randomUUID().toString());
        user = saveAndFlush(user);
        MailUtil.sendActivationEmail(user.getEmail(), user.getToken().getToken());
    }

    public void activate(User user) {
        user.setActive(true);
        user.setToken((Token) null);
        save(user);
    }

    public void activate(UserContext ctx, Long userId) {
        activate(findOneAdmin(ctx, userId));
    }

    public User findByEmail(String email) throws GamesException {
        return getRepository().findOneByEmail(email).orElseThrow(() -> new GamesException(GamesException.GamesErrors.EMAIL_NOT_EXISTS));
    }

    public void resendToken(User user) throws GamesException {
        if (user.isActive()) {
            throw new GamesException(GamesException.GamesErrors.USER_ALREADY_ACTIVE);
        }
        user.setToken(UUID.randomUUID().toString());
        user = saveAndFlush(user);
        MailUtil.sendActivationEmail(user.getEmail(), user.getToken().getToken());
    }

}
