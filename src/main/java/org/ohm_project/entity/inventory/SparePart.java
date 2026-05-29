package org.ohm_project.entity.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.ohm_project.entity.base.BaseAuditableEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "spare_parts", uniqueConstraints = {
                @UniqueConstraint(name = "uq_part_code_deleted", columnNames = { "part_code", "deleted_at" })
})
@SQLDelete(sql = "UPDATE spare_parts SET deleted_at = CURRENT_TIMESTAMP WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL")
public class SparePart extends BaseAuditableEntity {

        @NotBlank
        @Column(name = "part_code", nullable = false, length = 50)
        private String partCode; // Mã phụ tùng, kết hợp deleted_at thành Unique Key

        @NotBlank
        @Column(nullable = false, length = 150)
        private String name; // Tên phụ tùng

        @NotNull
        @Min(0)
        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal price; // Giá tiền

        @NotNull
        @Min(0)
        @Column(name = "stock_quantity", nullable = false)
        private Integer stockQuantity = 0; // Số lượng tồn kho thực tế

        @NotNull
        @Min(0)
        @Column(name = "warning_threshold", nullable = false)
        private Integer warningThreshold = 5; // Ngưỡng cảnh báo sắp hết hàng

        @Version
        @Column(nullable = false)
        private Integer version = 0; // Optimistic Locking Version để nếu có 2 thợ cùng ấn nút xuất 1 cái Item cuối
                                     // cùng trong kho
        // thì database sẽ từ chối 1 trong 2 request và cảnh báo, chặn tình trạng báo âm
        // kho.
}
