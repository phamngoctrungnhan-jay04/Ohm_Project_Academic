package org.ohm_project.entity.maintenance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ohm_project.entity.base.BaseAuditableEntity;
import org.ohm_project.enums.RepairOrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "repair_orders")
@SQLDelete(sql = "UPDATE repair_orders SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
// lệnh sửa chữa, đây là phiếu giao việc, bao gồm thông tin về yêu cầu sửa chữa
public class RepairOrder extends BaseAuditableEntity {

    @NotNull
    @Column(name = "reception_id", nullable = false, unique = true)
    private Long receptionId; // cầu nối khoá ngoại với phiếu giao nhận xe (vehicle_receptions)

    @Column(name = "assigned_technician_id")
    private Long assignedTechnicianId; // người thợ được giao việc

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RepairOrderStatus status; // vòng đời công việc

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
