package com.bank.bankingsystem.AuditLogService.service;

import com.bank.bankingsystem.AuditLogService.model.LoginLogEntity;
import com.bank.bankingsystem.AuditLogService.repository.LoginLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogService {
    private final LoginLogRepository repository;

    public LoginLogService(LoginLogRepository repository){
        this.repository = repository;
    }

    public List<LoginLogEntity> getLoginHistoryForUser(Long userId){
        return repository.findAllByUserId(userId);
    }
}
