package org.ohm_project.service.auth;

import org.ohm_project.dto.auth.LoginRequest;
import org.ohm_project.dto.auth.RegisterRequest;
import org.ohm_project.dto.auth.TokenResponse;
import org.ohm_project.dto.auth.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}
