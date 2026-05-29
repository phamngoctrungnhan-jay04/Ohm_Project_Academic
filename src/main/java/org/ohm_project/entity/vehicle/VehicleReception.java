package org.ohm_project.entity.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "vehicle_receptions")
@SQLDelete(sql = "UPDATE vehicle_receptions SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
// đây là bảng ghi thực tế tình trạng xe để đưa cho kỹ thuật viên kiểm tra
public class VehicleReception extends BaseAuditableEntity {

    @Column(name = "booking_id")
    private Long bookingId; // là id của booking tương ứng

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String licensePlate;

    @NotNull
    @Column(nullable = false)
    private Integer odometer; // là căn cứ để hệ thống quyết định xe có còn bảo hành ko, AI sẽ dùng số này để
                              // dự đoán khi nào cần thay phụ tùng

    @Column(precision = 5, scale = 2)
    private BigDecimal batterySoh; // chỉ sức khoẻ của pin, gồm 5 chữ số trong đó có 2 số thập phân, ví dụ: 100.00
                                   // thường từ 90% trở xuống là bắt đầu cần chú ý

    @Column(columnDefinition = "TEXT")
    private String exteriorStatus; // ghi nhận tình trạng bên ngoài của xe khi nhận
}
