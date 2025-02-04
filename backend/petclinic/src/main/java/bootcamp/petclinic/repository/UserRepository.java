package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(UUID uuid);

    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);

    boolean existsByAccountNumber(String accountNumber);
}
