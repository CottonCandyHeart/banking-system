package com.bank.bankingsystem.AuditLogService.controller;

import com.bank.bankingsystem.AuditLogService.model.LoginLogEntity;
import com.bank.bankingsystem.AuditLogService.service.LoginLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logins")
public class LoginLogController {
    private final LoginLogService loginLogService;

    public LoginLogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{userId}")
    public List<LoginLogEntity> getLoginLogs(@PathVariable Long userId){
        return loginLogService.getLoginHistoryForUser(userId);
    }
}

