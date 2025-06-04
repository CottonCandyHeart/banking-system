package com.bank.bankingsystem.CardSecurityService.repository;

import com.bank.bankingsystem.CardSecurityService.model.CardSecuritySessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardSecuritySessionRepository extends JpaRepository<CardSecuritySessionEntity, Long> {
    Optional<CardSecuritySessionEntity> findTopByUserIdOrderByAuthorizedAtDesc(Long userId);
}

