package org.ohm_project.dto.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String licensePlate;
    private LocalDateTime appointmentTime;
    private String serviceType;
    private String notes;
    private String status;
}
