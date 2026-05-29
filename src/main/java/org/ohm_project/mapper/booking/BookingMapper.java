package org.ohm_project.mapper.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ohm_project.dto.booking.BookingCreateRequest;
import org.ohm_project.dto.booking.BookingResponse;
import org.ohm_project.entity.auth.User;
import org.ohm_project.entity.booking.Booking;
import org.ohm_project.mapper.common.BaseMapperConfig;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface BookingMapper {

    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "id", source = "booking.id")
    @Mapping(target = "customerId", source = "booking.customerId")
    @Mapping(target = "status", source = "booking.status")
    BookingResponse toResponse(Booking booking, User customer);

    Booking toEntity(BookingCreateRequest request);

    void updateEntityFromRequest(BookingCreateRequest request, @MappingTarget Booking booking);

    BookingResponse toResponse(Booking booking);

    List<BookingResponse> toResponseList(List<Booking> bookings);
}
