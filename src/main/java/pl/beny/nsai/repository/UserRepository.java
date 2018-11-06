package pl.beny.nsai.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import pl.beny.nsai.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @EntityGraph(value = User.EntityGraphs.WITH_ROLES)
    Optional<User> findOneByEmail(String email);

    @EntityGraph(value = User.EntityGraphs.WITH_ROLES)
    List<User> findAll();

    @EntityGraph(value = User.EntityGraphs.WITH_ROLES)
    Optional<User> findOneById(Long Id);

}
