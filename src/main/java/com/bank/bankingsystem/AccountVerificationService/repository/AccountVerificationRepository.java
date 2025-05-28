package com.bank.bankingsystem.AccountVerificationService.repository;

import com.bank.bankingsystem.AccountVerificationService.model.VerifiedAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountVerificationRepository extends JpaRepository<VerifiedAccountEntity, String> {
    boolean existsByAccountNumber(String accountNumber);
}
