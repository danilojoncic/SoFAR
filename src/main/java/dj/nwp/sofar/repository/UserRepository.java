package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.SUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SUser, Long> {
    Optional<SUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
