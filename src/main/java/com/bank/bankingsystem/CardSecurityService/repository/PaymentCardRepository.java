package com.bank.bankingsystem.CardSecurityService.repository;

import com.bank.bankingsystem.CardSecurityService.model.PaymentCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCardEntity, Long> {
    Optional<PaymentCardEntity> findByUserId(Long userId);
}