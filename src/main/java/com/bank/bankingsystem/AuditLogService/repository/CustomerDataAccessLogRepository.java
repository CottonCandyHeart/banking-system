package com.bank.bankingsystem.AuditLogService.repository;

import com.bank.bankingsystem.AuditLogService.model.CustomerDataAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDataAccessLogRepository extends JpaRepository<CustomerDataAccessLog, Long> {
}
