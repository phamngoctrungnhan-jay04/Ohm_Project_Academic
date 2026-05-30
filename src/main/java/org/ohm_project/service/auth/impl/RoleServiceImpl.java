package org.ohm_project.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohm_project.entity.auth.Role;
import org.ohm_project.entity.auth.User;
import org.ohm_project.entity.auth.UserRole;
import org.ohm_project.entity.auth.UserRoleId;
import org.ohm_project.enums.RoleName;
import org.ohm_project.exception.AppException;
import org.ohm_project.exception.ErrorCode;
import org.ohm_project.repository.auth.RoleRepository;
import org.ohm_project.repository.auth.UserRoleRepository;
import org.ohm_project.service.auth.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public void assignRoleToUser(User user, RoleName roleName) {
        log.info("Assigning role {} to user {}", roleName, user.getUsername());

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.error("Role {} not found in database", roleName);
                    return new AppException(ErrorCode.ROLE_NOT_FOUND);
                });

        UserRole userRole = new UserRole();
        
        // Setup composite ID
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(user.getId());
        userRoleId.setRoleId(role.getId());
        userRole.setId(userRoleId);
        
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
        log.info("Successfully assigned role {} to user {}", roleName, user.getUsername());
    }
}
