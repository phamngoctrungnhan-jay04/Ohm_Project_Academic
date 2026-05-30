package org.ohm_project.service.auth;

import org.ohm_project.dto.auth.RegisterRequest;
import org.ohm_project.entity.auth.User;

public interface UserService {
    User createUser(RegisterRequest request);
    User getUserByUsername(String username);
}
