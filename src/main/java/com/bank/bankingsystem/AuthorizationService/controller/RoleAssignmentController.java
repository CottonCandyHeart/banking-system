package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.RoleUpdateRequest;
import com.bank.bankingsystem.AuthorizationService.service.RoleAssignmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class RoleAssignmentController {

    private final RoleAssignmentService service;

    public RoleAssignmentController(RoleAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/assign-role")
    public void assignRole(@RequestBody RoleUpdateRequest request) {
        service.assignRole(request.getEmployeeId(), request.getNewRole());
    }
}
