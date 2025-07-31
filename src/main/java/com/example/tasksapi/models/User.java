package com.example.tasksapi.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean emailVerified;
    private boolean accountLocked;
    private int failedLoginAttempts;
    private LocalDateTime lastLogin;
    private String status; // active/suspended/deleted
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
