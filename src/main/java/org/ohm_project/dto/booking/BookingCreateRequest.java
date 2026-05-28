package org.ohm_project.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingCreateRequest {
    @NotBlank(message = "License plate is required")
    @Size(max = 20, message = "License plate cannot exceed 20 characters")
    private String licensePlate;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Service type is required")
    @Size(max = 50, message = "Service type cannot exceed 50 characters")
    private String serviceType;

    private String notes;
}
