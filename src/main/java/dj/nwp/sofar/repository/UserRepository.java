package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
