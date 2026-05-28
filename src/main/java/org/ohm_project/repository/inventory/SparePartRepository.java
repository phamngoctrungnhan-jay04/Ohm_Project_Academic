package org.ohm_project.repository.inventory;

import org.ohm_project.entity.inventory.SparePart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    boolean existsByPartCode(String partCode);
    Optional<SparePart> findByPartCode(String partCode);
    Page<SparePart> findAll(Pageable pageable);
}
