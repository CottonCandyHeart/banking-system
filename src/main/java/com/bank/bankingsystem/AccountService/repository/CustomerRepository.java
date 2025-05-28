package com.bank.bankingsystem.AccountService.repository;

import com.bank.bankingsystem.AccountService.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findById(Long id);

    boolean existsByAccountNumber(String accountNumber);
}

