package org.ohm_project.repository.auth;

import org.ohm_project.entity.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
