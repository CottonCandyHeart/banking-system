package com.bank.bankingsystem.AccountVerificationService.service;

import com.bank.bankingsystem.AccountService.repository.CustomerRepository;
import com.bank.bankingsystem.AccountVerificationService.dto.AccountVerificationRequest;
import com.bank.bankingsystem.AccountVerificationService.dto.AccountVerificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountVerificationServiceUnitTest {
    private CustomerRepository customerRepository;
    private AccountVerificationService accountVerificationService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        accountVerificationService = new AccountVerificationService(customerRepository);
    }

    @Test
    void shouldReturnInvalidWhenAccountNumberIsNull() {
        AccountVerificationRequest request = new AccountVerificationRequest(null);

        AccountVerificationResponse response = accountVerificationService.verifyAccount(request);

        assertFalse(response.isValid());
        assertEquals("Incorrect account number format.", response.getMessage());
    }

    @Test
    void shouldReturnInvalidWhenAccountNumberIsIncorrectFormat() {
        AccountVerificationRequest request = new AccountVerificationRequest("123456");

        AccountVerificationResponse response = accountVerificationService.verifyAccount(request);

        assertFalse(response.isValid());
        assertEquals("Incorrect account number format.", response.getMessage());
    }

    @Test
    void shouldReturnInvalidWhenAccountNumberDoesNotExist() {
        String testAccount = "12345678901234567890123456";
        when(customerRepository.existsByAccountNumber(testAccount)).thenReturn(false);

        AccountVerificationRequest request = new AccountVerificationRequest(testAccount);
        AccountVerificationResponse response = accountVerificationService.verifyAccount(request);

        assertFalse(response.isValid());
        assertEquals("The account number does not exist in the system.", response.getMessage());
    }

    @Test
    void shouldReturnValidWhenAccountNumberExistsAndFormatIsCorrect() {
        String testAccount = "12345678901234567890123456";
        when(customerRepository.existsByAccountNumber(testAccount)).thenReturn(true);

        AccountVerificationRequest request = new AccountVerificationRequest(testAccount);
        AccountVerificationResponse response = accountVerificationService.verifyAccount(request);

        assertTrue(response.isValid());
        assertEquals("The account number is correct.", response.getMessage());
    }
}
