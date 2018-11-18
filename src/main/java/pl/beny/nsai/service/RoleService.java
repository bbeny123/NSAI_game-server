package pl.beny.nsai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.beny.nsai.model.Role;
import pl.beny.nsai.repository.RoleRepository;
import pl.beny.nsai.util.GamesException;

@Service
public class RoleService extends BaseService<Role> {

    private RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Role findByRole(Role.Roles role) throws GamesException {
        return repository.findByRole(role).orElseThrow(() -> new GamesException(GamesException.GamesErrors.ROLE_NOT_EXISTS));
    }

}
