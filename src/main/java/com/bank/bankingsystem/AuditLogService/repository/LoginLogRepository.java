package com.bank.bankingsystem.AuditLogService.repository;

import com.bank.bankingsystem.AuditLogService.model.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Long> {
    List<LoginLogEntity> findAllByUserId(Long userId);
}
