package org.ohm_project.repository.maintenance;

import org.ohm_project.entity.maintenance.MaintenanceChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MaintenanceChecklistRepository extends JpaRepository<MaintenanceChecklist, Long> {
    @Query("SELECT c FROM MaintenanceChecklist c JOIN FETCH c.items WHERE c.repairOrderId = :repairOrderId")
    Optional<MaintenanceChecklist> findByRepairOrderIdWithItems(@Param("repairOrderId") Long repairOrderId);
}
