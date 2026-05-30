package org.ohm_project.service.auth;

import org.ohm_project.entity.auth.User;
import org.ohm_project.enums.RoleName;

public interface RoleService {
    void assignRoleToUser(User user, RoleName roleName);
}
