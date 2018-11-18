package pl.beny.nsai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.beny.nsai.model.Token;
import pl.beny.nsai.model.User;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.repository.UserRepository;
import pl.beny.nsai.util.MailUtil;
import pl.beny.nsai.util.GamesException;
import pl.beny.nsai.util.RoleUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService<User> {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public void create(User user) throws GamesException {
        if (existsByEmail(user.getEmail())) throw new GamesException(GamesException.GamesErrors.USER_EXISTS);
        user.setRoles(Collections.singleton(RoleUtil.findUser()));
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

    public void activate(UserContext ctx, Long userId) throws GamesException {
        User user = findOneAdmin(ctx, userId);
        user.setActive(true);
        user.setToken((Token) null);
        save(user);
    }

    public User findByEmail(String email) throws GamesException {
        return repository.findByEmail(email).orElseThrow(() -> new GamesException(GamesException.GamesErrors.EMAIL_NOT_EXISTS));
    }

    public void resendToken(User user) throws GamesException {
        if (user.isActive()) {
            throw new GamesException(GamesException.GamesErrors.USER_ALREADY_ACTIVE);
        }
        user.setToken(UUID.randomUUID().toString());
        user = saveAndFlush(user);
        MailUtil.sendActivationEmail(user.getEmail(), user.getToken().getToken());
    }

    @Override
    public List<User> findAllAdmin(UserContext ctx) throws GamesException {
        checkAdmin(ctx);
        return repository.findAll();
    }

    public void changeRole(UserContext ctx, Long userId, String action, String role) throws GamesException {
        checkAdmin(ctx);
        User user = repository.findOneById(userId).orElseThrow(() -> new GamesException(GamesException.GamesErrors.USER_NOT_EXISTS));
        if (user.getId().equals(ctx.getUser().getId())) {
            throw new GamesException(GamesException.GamesErrors.NOT_AUTHORIZED);
        }
        if (User.Action.GRANT.equalsIgnoreCase(action)) {
            grantRole(user, role);
        } else if (User.Action.REVOKE.equalsIgnoreCase(action)) {
            revokeRole(user, role);
        }
        saveAdmin(ctx, user);
    }

    private void grantRole(User user, String r) throws GamesException {
        if(user.getRoles().stream().noneMatch(role -> role.getRole().getRole().equalsIgnoreCase(r))) {
            user.getRoles().add(RoleUtil.findRole(r));
        }
    }

    private void revokeRole(User user, String r) {
        user.getRoles().removeIf(role -> role.getRole().getRole().equalsIgnoreCase(r));
    }

}
