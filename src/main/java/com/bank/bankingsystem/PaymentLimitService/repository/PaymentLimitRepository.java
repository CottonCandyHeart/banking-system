package com.bank.bankingsystem.PaymentLimitService.repository;

import com.bank.bankingsystem.PaymentLimitService.model.PaymentLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentLimitRepository extends JpaRepository<PaymentLimitEntity, Long> {
    Optional<PaymentLimitEntity> findByCustomerId(Long customerId);
}
