package com.bank.bankingsystem.AuditLogService.service;

import com.bank.bankingsystem.AuditLogService.model.CustomerDataAccessLog;
import com.bank.bankingsystem.AuditLogService.repository.CustomerDataAccessLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerDataAccessLogServiceTest {

    private CustomerDataAccessLogRepository repository;
    private CustomerDataAccessLogService service;

    @BeforeEach
    void setUp() {
        repository = mock(CustomerDataAccessLogRepository.class);
        service = new CustomerDataAccessLogService(repository);
    }

    @Test
    void shouldLogAccessAndSaveToRepository() {
        Long employeeId = 1L;
        Long customerId = 2L;

        service.logAccess(employeeId, customerId);

        ArgumentCaptor<CustomerDataAccessLog> captor = ArgumentCaptor.forClass(CustomerDataAccessLog.class);
        verify(repository, times(1)).save(captor.capture());

        CustomerDataAccessLog savedLog = captor.getValue();
        assertEquals(employeeId, savedLog.getEmployeeId());
        assertEquals(customerId, savedLog.getCustomerId());
        assertNotNull(savedLog.getAccessTime());

        assertTrue(savedLog.getAccessTime().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}