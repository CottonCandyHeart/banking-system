package com.bank.bankingsystem.AuthorizationService.repository;

import com.bank.bankingsystem.AuthorizationService.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {}
