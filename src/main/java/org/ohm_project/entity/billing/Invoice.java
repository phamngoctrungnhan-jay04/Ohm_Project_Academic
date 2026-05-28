package org.ohm_project.entity.billing;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ohm_project.entity.base.BaseAuditableEntity;
import org.ohm_project.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "invoices")
@SQLDelete(sql = "UPDATE invoices SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Invoice extends BaseAuditableEntity {

    @NotNull
    @Column(name = "repair_order_id", nullable = false, unique = true)
    private Long repairOrderId; // Tham chiếu ID về Lệnh sửa chữa

    @NotBlank
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber; // Mã hóa đơn định dạng (Ví dụ: INV-2026-00001)

    @NotNull
    @Min(0)
    @Column(name = "sub_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal subTotal; // Tổng tiền trước thuế

    @NotNull
    @Min(0)
    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount; // Tiền thuế VAT

    @NotNull
    @Min(0)
    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount; // Tiền chiết khấu / giảm giá

    @NotNull
    @Min(0)
    @Column(name = "grand_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal grandTotal; // Tổng tiền phải thu của khách hàng

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private InvoiceStatus paymentStatus; // UNPAID, PAID

    @Column(name = "payment_method", length = 30)
    private String paymentMethod; // Phương thức thanh toán: CASH, BANK_TRANSFER, CREDIT_CARD

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // Thời điểm hoàn tất thu tiền
}
