package org.ohm_project.entity.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ohm_project.entity.base.BaseEntity;
import org.ohm_project.enums.RoleName;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;

    @Column(length = 255)
    private String description;
}
