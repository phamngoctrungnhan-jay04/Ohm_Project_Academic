package org.ohm_project.entity.maintenance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ohm_project.entity.base.BaseEntity;
import org.ohm_project.enums.ChecklistItemStatus;

@Getter
@Setter
@Entity
@Table(name = "maintenance_checklist_items")
// chi tiết kết quả làm việc của thợ trên từng mục
public class MaintenanceChecklistItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_checklist_id", nullable = false)
    private MaintenanceChecklist checklist; // là id của checklist tương ứng

    @NotNull
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ChecklistItemStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "evidence_url", length = 512)
    private String evidenceUrl; // chứa link aws s3 của bức ảnh bằng chứng, do thợ chụp và upload lên khi check
}
