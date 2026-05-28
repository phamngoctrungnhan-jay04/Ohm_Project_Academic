package org.ohm_project.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoicePaymentRequest {
    @NotBlank(message = "Payment method is required")
    @Size(max = 30, message = "Payment method cannot exceed 30 characters")
    private String paymentMethod;
}
