package com.bank.bankingsystem.AuditLogService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CustomerDataAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Long customerId;
    private LocalDateTime accessTime;

    public CustomerDataAccessLog() {}

    public CustomerDataAccessLog(Long employeeId, Long customerId, LocalDateTime accessTime) {
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.accessTime = accessTime;
    }
}
