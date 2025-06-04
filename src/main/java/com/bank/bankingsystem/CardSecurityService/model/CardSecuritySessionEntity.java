package com.bank.bankingsystem.CardSecurityService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CardSecuritySessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDateTime authorizedAt;

    public CardSecuritySessionEntity() {}

    public CardSecuritySessionEntity(Long userId, LocalDateTime authorizedAt) {
        this.userId = userId;
        this.authorizedAt = authorizedAt;
    }

}
