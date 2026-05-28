package org.ohm_project.repository.maintenance;

import org.ohm_project.entity.maintenance.ChecklistTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Long> {
    boolean existsByCode(String code);

    @Query("SELECT t FROM ChecklistTemplate t JOIN FETCH t.items WHERE t.code = :code")
    Optional<ChecklistTemplate> findByCodeWithItems(@Param("code") String code);
}
