package com.bank.bankingsystem.AccountService.service;

import com.bank.bankingsystem.AccountService.dto.CustomerDataDTO;
import com.bank.bankingsystem.AccountService.model.CustomerEntity;
import com.bank.bankingsystem.AccountService.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerDataServiceUnitTest {
    private CustomerRepository customerRepository;
    private CustomerDataService customerDataService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerDataService = new CustomerDataService(customerRepository);
    }

    @Test
    void shouldReturnCustomerDataWhenCustomerExists() {
        Long customerId = 1L;
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setFullName("Test Test");
        customerEntity.setEmail("test@example.com");
        customerEntity.setAddress("Test 123");
        customerEntity.setPhoneNumber("123456789");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

        CustomerDataDTO result = customerDataService.getCustomerData(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Test Test", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test 123", result.getAddress());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        Long customerId = 2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                customerDataService.getCustomerData(customerId));
        assertEquals("Customer not found", exception.getMessage());
    }
}
