package org.ohm_project.mapper.vehicle;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ohm_project.dto.vehicle.VehicleReceptionCreateRequest;
import org.ohm_project.dto.vehicle.VehicleReceptionResponse;
import org.ohm_project.entity.auth.User;
import org.ohm_project.entity.vehicle.VehicleReception;
import org.ohm_project.mapper.common.BaseMapperConfig;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface VehicleReceptionMapper {

    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerPhone", source = "customer.phone")
    @Mapping(target = "id", source = "reception.id")
    @Mapping(target = "customerId", source = "reception.customerId")
    @Mapping(target = "createdAt", source = "reception.createdAt")
    VehicleReceptionResponse toResponse(VehicleReception reception, User customer);

    VehicleReception toEntity(VehicleReceptionCreateRequest request);

    void updateEntityFromRequest(VehicleReceptionCreateRequest request, @MappingTarget VehicleReception reception);

    VehicleReceptionResponse toResponse(VehicleReception reception);

    List<VehicleReceptionResponse> toResponseList(List<VehicleReception> receptions);
}
