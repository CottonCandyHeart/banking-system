package com.bank.bankingsystem.AuthorizationService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
}
