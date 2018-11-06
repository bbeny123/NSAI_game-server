package pl.beny.nsai.repository;

import org.springframework.stereotype.Repository;
import pl.beny.nsai.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByRole(Role.Roles role);

}
