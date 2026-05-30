package org.ohm_project.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohm_project.dto.auth.RegisterRequest;
import org.ohm_project.entity.auth.User;
import org.ohm_project.enums.UserStatus;
import org.ohm_project.exception.AppException;
import org.ohm_project.exception.ErrorCode;
import org.ohm_project.mapper.auth.UserMapper;
import org.ohm_project.repository.auth.UserRepository;
import org.ohm_project.service.auth.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(RegisterRequest request) {
        log.info("Creating new user with username: {}", request.getUsername());

        // 1. Validate uniqueness
        if (userRepository.existsByUsername(request.getUsername()) ||
            userRepository.existsByEmail(request.getEmail()) ||
            userRepository.existsByPhone(request.getPhone())) {
            
            log.warn("User creation failed: Data already exists for username {}, email {} or phone {}", 
                     request.getUsername(), request.getEmail(), request.getPhone());
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 2. Map and encode password
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        // 3. Save to DB
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
    }
}
