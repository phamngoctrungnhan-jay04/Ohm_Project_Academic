package org.ohm_project.entity.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ohm_project.entity.base.BaseAuditableEntity;
import org.ohm_project.enums.UserStatus;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_username_deleted", columnNames = {"username", "deleted_at"}),
                @UniqueConstraint(name = "uq_email_deleted", columnNames = {"email", "deleted_at"}),
                @UniqueConstraint(name = "uq_phone_deleted", columnNames = {"phone", "deleted_at"})
        }
)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseAuditableEntity {

    @NotBlank
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 15)
    private String phone;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;
}
