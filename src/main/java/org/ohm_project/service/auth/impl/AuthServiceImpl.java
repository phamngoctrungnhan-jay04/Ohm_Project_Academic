package org.ohm_project.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohm_project.dto.auth.LoginRequest;
import org.ohm_project.dto.auth.RegisterRequest;
import org.ohm_project.dto.auth.TokenResponse;
import org.ohm_project.dto.auth.UserResponse;
import org.ohm_project.entity.auth.User;
import org.ohm_project.enums.RoleName;
import org.ohm_project.exception.AppException;
import org.ohm_project.exception.ErrorCode;
import org.ohm_project.mapper.auth.UserMapper;
import org.ohm_project.service.auth.AuthService;
import org.ohm_project.service.auth.JwtService;
import org.ohm_project.service.auth.RoleService;
import org.ohm_project.service.auth.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Processing registration for: {}", request.getUsername());

        // 1. Create User via UserService
        User newUser = userService.createUser(request);

        // 2. Assign Default Role (CUSTOMER) via RoleService
        roleService.assignRoleToUser(newUser, RoleName.ROLE_CUSTOMER);

        // 3. Map to Response and manually set role
        UserResponse response = userMapper.toResponse(newUser);
        response.setRoles(Collections.singletonList(RoleName.ROLE_CUSTOMER.name()));

        log.info("Registration completed successfully for: {}", request.getUsername());
        return response;
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        log.info("Processing login for: {}", request.getUsername());

        // 1. Fetch user (Throws INVALID_CREDENTIALS if not found)
        User user = userService.getUserByUsername(request.getUsername());

        // 2. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", request.getUsername());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. Generate tokens via JwtService
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("Login successful for: {}", request.getUsername());
        return new TokenResponse(accessToken, refreshToken, jwtService.getExpirationTime());
    }
}
