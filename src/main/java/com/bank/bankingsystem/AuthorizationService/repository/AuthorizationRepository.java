package com.bank.bankingsystem.AuthorizationService.repository;

import com.bank.bankingsystem.AuthorizationService.model.AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationRepository extends JpaRepository<AuthorizationEntity, Long> {
    Optional<AuthorizationEntity> findByUsername(String username);
}
