package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByActivationToken(String activationToken);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNif(String nif);
}
