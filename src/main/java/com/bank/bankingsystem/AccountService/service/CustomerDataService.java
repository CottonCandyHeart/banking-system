package com.bank.bankingsystem.AccountService.service;

import com.bank.bankingsystem.AccountService.dto.CustomerDataDTO;
import com.bank.bankingsystem.AccountService.model.CustomerEntity;
import com.bank.bankingsystem.AccountService.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerDataService {

    private final CustomerRepository customerRepository;

    public CustomerDataService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDataDTO getCustomerData(Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new CustomerDataDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
    }
}

