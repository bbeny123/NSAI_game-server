package pl.beny.nsai.repository;

import org.springframework.stereotype.Repository;
import pl.beny.nsai.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    boolean existsByEmail(String email);

    Optional<User> findOneByEmail(String email);

}
