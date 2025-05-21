package com.bank.bankingsystem.AccountService.controller;

import com.bank.bankingsystem.AccountService.dto.CustomerDataDTO;
import com.bank.bankingsystem.AccountService.service.CustomerDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerDataController {
    private final CustomerDataService customerDataService;

    public CustomerDataController(CustomerDataService customerDataService) {
        this.customerDataService = customerDataService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{userId}")
    public ResponseEntity<CustomerDataDTO> getCustomerData(@PathVariable Long userId) {
        return ResponseEntity.ok(customerDataService.getCustomerData(userId));
    }
}
