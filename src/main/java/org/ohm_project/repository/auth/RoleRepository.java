package org.ohm_project.repository.auth;

import org.ohm_project.entity.auth.Role;
import org.ohm_project.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
