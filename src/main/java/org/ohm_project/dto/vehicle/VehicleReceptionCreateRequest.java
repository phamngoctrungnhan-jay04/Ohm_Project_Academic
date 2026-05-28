package org.ohm_project.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VehicleReceptionCreateRequest {
    private Long bookingId;

    @NotBlank(message = "License plate is required")
    @Size(max = 20, message = "License plate cannot exceed 20 characters")
    private String licensePlate;

    @NotNull(message = "Odometer is required")
    @Min(value = 0, message = "Odometer cannot be negative")
    private Integer odometer;

    @Min(value = 0, message = "Battery SOH cannot be negative")
    private BigDecimal batterySoh;

    private String exteriorStatus;
}
