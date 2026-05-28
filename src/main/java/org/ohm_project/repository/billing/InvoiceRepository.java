package org.ohm_project.repository.billing;

import org.ohm_project.entity.billing.Invoice;
import org.ohm_project.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Optional<Invoice> findByRepairOrderId(Long repairOrderId);
    Page<Invoice> findAllByPaymentStatus(InvoiceStatus paymentStatus, Pageable pageable);
}
