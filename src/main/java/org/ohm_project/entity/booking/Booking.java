package org.ohm_project.entity.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ohm_project.entity.base.BaseAuditableEntity;
import org.ohm_project.enums.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bookings")
@SQLDelete(sql = "UPDATE bookings SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Booking extends BaseAuditableEntity {

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String licensePlate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String serviceType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status;
}
