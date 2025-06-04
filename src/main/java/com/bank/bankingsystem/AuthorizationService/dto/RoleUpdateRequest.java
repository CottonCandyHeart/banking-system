package com.bank.bankingsystem.AuthorizationService.dto;

public class RoleUpdateRequest {
    private Long employeeId;
    private String newRole;

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getNewRole() { return newRole; }
    public void setNewRole(String newRole) { this.newRole = newRole; }
}
