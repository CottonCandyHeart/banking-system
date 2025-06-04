package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.model.EmployeeEntity;
import com.bank.bankingsystem.AuthorizationService.model.RoleEntity;
import com.bank.bankingsystem.AuthorizationService.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleAssignmentServiceUnitTest {

    private EmployeeRepository employeeRepository;
    private RoleAssignmentService roleAssignmentService;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        roleAssignmentService = new RoleAssignmentService(employeeRepository);
    }

    @Test
    void shouldAssignRoleToEmployeeSuccessfully() {
        Long employeeId = 1L;
        String roleName = "ADMIN";
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        roleAssignmentService.assignRole(employeeId, roleName);

        ArgumentCaptor<EmployeeEntity> captor = ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(captor.capture());
        EmployeeEntity savedEmployee = captor.getValue();

        assertEquals(RoleEntity.ADMIN, savedEmployee.getRole());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        Long employeeId = 99L;
        String roleName = "EMPLOYEE";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                roleAssignmentService.assignRole(employeeId, roleName)
        );

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        Long employeeId = 1L;
        String invalidRole = "HACKER";
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        assertThrows(IllegalArgumentException.class, () ->
                roleAssignmentService.assignRole(employeeId, invalidRole)
        );

        verify(employeeRepository, never()).save(any());
    }
}