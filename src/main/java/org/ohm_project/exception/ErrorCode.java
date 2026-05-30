package org.ohm_project.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    USER_ALREADY_EXISTS(400, "AUTH_001", "User already exists with given username, email or phone"),
    ROLE_NOT_FOUND(400, "AUTH_002", "Role not found in database"),
    INVALID_CREDENTIALS(400, "AUTH_003", "Invalid username or password");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
