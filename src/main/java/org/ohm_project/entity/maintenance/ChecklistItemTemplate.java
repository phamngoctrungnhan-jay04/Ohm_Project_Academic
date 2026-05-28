package org.ohm_project.entity.maintenance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ohm_project.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "checklist_item_templates")
// chi tiết từng mục trong danh mục kiểm tra, do admin tạo sẵn
public class ChecklistItemTemplate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ChecklistTemplate template;

    @NotNull
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "require_evidence", nullable = false)
    private Boolean requireEvidence = false; // bắt buộc phải có ảnh hay không
}
