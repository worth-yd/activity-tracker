
package worthyd.com.example.activity_tracker.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import worthyd.com.example.activity_tracker.auth.model.UserEntity;
import worthyd.com.example.activity_tracker.auth.repository.*;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  boolean existsByUsername(String username);
}
