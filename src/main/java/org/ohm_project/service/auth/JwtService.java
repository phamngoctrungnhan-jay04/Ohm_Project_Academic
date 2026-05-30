package org.ohm_project.service.auth;

import org.ohm_project.entity.auth.User;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    long getExpirationTime();
}
