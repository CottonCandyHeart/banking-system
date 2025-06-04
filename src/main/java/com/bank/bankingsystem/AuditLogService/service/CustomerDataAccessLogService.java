package com.bank.bankingsystem.AuditLogService.service;

import com.bank.bankingsystem.AuditLogService.model.CustomerDataAccessLog;
import com.bank.bankingsystem.AuditLogService.repository.CustomerDataAccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerDataAccessLogService {

    private final CustomerDataAccessLogRepository repository;

    public CustomerDataAccessLogService(CustomerDataAccessLogRepository repository) {
        this.repository = repository;
    }

    public void logAccess(Long employeeId, Long customerId) {
        CustomerDataAccessLog log = new CustomerDataAccessLog(employeeId, customerId, LocalDateTime.now());
        repository.save(log);
    }
}
