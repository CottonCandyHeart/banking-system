package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.model.EmployeeEntity;
import com.bank.bankingsystem.AuthorizationService.model.RoleEntity;
import com.bank.bankingsystem.AuthorizationService.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleAssignmentService {

    private final EmployeeRepository repository;

    public RoleAssignmentService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void assignRole(Long employeeId, String roleName) {
        EmployeeEntity employee = repository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        RoleEntity role = RoleEntity.valueOf(roleName.toUpperCase());
        employee.setRole(role);
        repository.save(employee);
    }
}
