package org.ohm_project.dto.vehicle;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VehicleReceptionResponse {
    private Long id;
    private Long bookingId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String licensePlate;
    private Integer odometer;
    private BigDecimal batterySoh;
    private String exteriorStatus;
    private LocalDateTime createdAt;
}
