package com.bank.bankingsystem.AuthorizationService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class AuthorizationEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String passwordHash;
    private boolean active;

    // failed login attempts
    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;
    @Column(name = "lock_time")
    private LocalDateTime lockTime;
}
