package org.ohm_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Username or Email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
