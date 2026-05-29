package org.ohm_project.mapper.invoice;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ohm_project.dto.invoice.InvoicePaymentRequest;
import org.ohm_project.dto.invoice.InvoiceResponse;
import org.ohm_project.entity.billing.Invoice;
import org.ohm_project.mapper.common.BaseMapperConfig;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface InvoiceMapper {

    InvoiceResponse toResponse(Invoice invoice);

    void updateEntityFromRequest(InvoicePaymentRequest request, @MappingTarget Invoice invoice);

    List<InvoiceResponse> toResponseList(List<Invoice> invoices);
}
