package org.ohm_project.repository.auth;

import org.ohm_project.entity.auth.UserRole;
import org.ohm_project.entity.auth.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.id.userId = :userId")
    List<UserRole> findAllByUserIdWithRole(@Param("userId") Long userId);
}
