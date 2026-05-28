package org.ohm_project.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String status;
    private List<String> roles;
}
