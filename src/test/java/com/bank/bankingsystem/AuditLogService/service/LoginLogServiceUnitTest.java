package com.bank.bankingsystem.AuditLogService.service;

import com.bank.bankingsystem.AuditLogService.model.LoginLogEntity;
import com.bank.bankingsystem.AuditLogService.repository.LoginLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class LoginLogServiceUnitTest {
    private LoginLogRepository repository;
    private LoginLogService loginLogService;

    @BeforeEach
    void setUp() {
        repository = mock(LoginLogRepository.class);
        loginLogService = new LoginLogService(repository);
    }

    @Test
    void shouldReturnLoginLogsForUser() {
        Long userId = 1L;
        LoginLogEntity log1 = new LoginLogEntity();
        log1.setId(1L);
        log1.setUserId(userId);
        log1.setLoginTime(LocalDateTime.now());
        log1.setIpAddress("127.0.0.1");
        log1.setSuccess(true);

        LoginLogEntity log2 = new LoginLogEntity();
        log2.setId(2L);
        log2.setUserId(userId);
        log2.setLoginTime(LocalDateTime.now().minusDays(1));
        log2.setIpAddress("192.168.1.1");
        log2.setSuccess(false);

        List<LoginLogEntity> mockLogs = List.of(log1, log2);
        when(repository.findAllByUserId(userId)).thenReturn(mockLogs);

        List<LoginLogEntity> result = loginLogService.getLoginHistoryForUser(userId);

        assertEquals(2, result.size());
        assertEquals("127.0.0.1", result.get(0).getIpAddress());
        assertEquals(false, result.get(1).isSuccess());

        verify(repository, times(1)).findAllByUserId(userId);
    }
}
