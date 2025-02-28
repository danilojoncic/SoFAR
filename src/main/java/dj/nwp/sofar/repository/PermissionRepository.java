package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByTitle(String title);
}
