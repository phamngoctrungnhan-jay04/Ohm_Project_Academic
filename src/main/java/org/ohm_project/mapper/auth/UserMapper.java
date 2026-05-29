package org.ohm_project.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ohm_project.dto.auth.RegisterRequest;
import org.ohm_project.dto.auth.UserResponse;
import org.ohm_project.entity.auth.User;
import org.ohm_project.mapper.common.BaseMapperConfig;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true) // Cần map thủ công thông qua Service
    UserResponse toResponse(User user);

    User toEntity(RegisterRequest request);

    void updateEntityFromRequest(RegisterRequest request, @MappingTarget User user);

    List<UserResponse> toResponseList(List<User> users);
}
