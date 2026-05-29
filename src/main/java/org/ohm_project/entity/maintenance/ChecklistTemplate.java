package org.ohm_project.entity.maintenance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.ohm_project.entity.base.BaseAuditableEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "checklist_templates")
@SQLDelete(sql = "UPDATE checklist_templates SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
// bìa mẫu danh mục kiểm tra, do admin tạo sẵn
public class ChecklistTemplate extends BaseAuditableEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String code; // ví dụ: CHECKLIST_A, dùng mã này để tìm kiếm

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name; // ví dụ: Danh mục kiểm tra xe A

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItemTemplate> items = new ArrayList<>(); // danh sách các mục trong checklist
}
