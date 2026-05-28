package org.ohm_project.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceResponse {
    private Long id;
    private Long repairOrderId;
    private String invoiceNumber;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
