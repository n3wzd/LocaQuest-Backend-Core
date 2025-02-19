package com.example.locaquest.model;

import java.time.LocalDateTime;

import com.example.locaquest.annotation.PasswordComplexity;
import com.example.locaquest.dto.group.CreateGroup;
import com.example.locaquest.dto.group.PasswordGroup;
import com.example.locaquest.dto.group.UpdateGroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private int userId;

    @Column(nullable = false, unique = true, updatable = false)
    @Email(message = "Email should be valid", groups = { CreateGroup.class })
    @NotBlank(message = "Email is required", groups = { CreateGroup.class })
    @Size(max = 320, message = "Email must be less than 320 characters", groups = { CreateGroup.class })
    private String email;

    @Column(name = "user_pw", nullable = false)
    @NotBlank(message = "Password is required", groups = { CreateGroup.class, PasswordGroup.class, UpdateGroup.class })
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = { CreateGroup.class, PasswordGroup.class, UpdateGroup.class })
    @Size(max = 60, message = "Password must be less than 60 characters", groups = { CreateGroup.class, PasswordGroup.class, UpdateGroup.class })
    @PasswordComplexity(message = "Password must contain at least one number and one special character", groups = { CreateGroup.class, PasswordGroup.class, UpdateGroup.class })
    private String password;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "Name is required", groups = { CreateGroup.class, UpdateGroup.class })
    @Size(max = 100, message = "Name must be less than 100 characters", groups = { CreateGroup.class, UpdateGroup.class })
    private String name;

    @Column(unique = true)
    @Size(max = 13, message = "Phone must be less than 13 characters", groups = UpdateGroup.class)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
