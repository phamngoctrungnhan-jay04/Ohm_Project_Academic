package org.ohm_project.repository.maintenance;

import org.ohm_project.entity.maintenance.RepairOrder;
import org.ohm_project.enums.RepairOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    Optional<RepairOrder> findByReceptionId(Long receptionId);
    List<RepairOrder> findAllByAssignedTechnicianIdAndStatus(Long technicianId, RepairOrderStatus status);
    Page<RepairOrder> findAllByStatus(RepairOrderStatus status, Pageable pageable);
}
