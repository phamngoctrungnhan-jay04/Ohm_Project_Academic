package org.ohm_project.entity.maintenance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ohm_project.entity.base.BaseAuditableEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "maintenance_checklists")
@SQLDelete(sql = "UPDATE maintenance_checklists SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
// danh sách checklist được tạo ra từ template, gắn với repair order cụ thể
public class MaintenanceChecklist extends BaseAuditableEntity {

    @NotNull
    @Column(name = "repair_order_id", nullable = false)
    private Long repairOrderId; // là id của repair order tương ứng

    @NotBlank
    @Column(name = "template_code", nullable = false, length = 50)
    private String templateCode; // là mã template được lấy từ checklist_templates

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceChecklistItem> items = new ArrayList<>(); // danh sách các mục trong checklist
}
