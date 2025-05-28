package com.bank.bankingsystem.AccountVerificationService.service;

import com.bank.bankingsystem.AccountService.repository.CustomerRepository;
import com.bank.bankingsystem.AccountVerificationService.dto.AccountVerificationRequest;
import com.bank.bankingsystem.AccountVerificationService.dto.AccountVerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AccountVerificationService {
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^\\d{26}$");

    @Autowired
    private CustomerRepository customerRepository;

    public AccountVerificationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public AccountVerificationResponse verifyAccount(AccountVerificationRequest request) {
        String accountNumber = request.getAccountNumber();

        if (accountNumber == null || !ACCOUNT_PATTERN.matcher(accountNumber).matches()) {
            return new AccountVerificationResponse(false, "Incorrect account number format.");
        }

        boolean exists = customerRepository.existsByAccountNumber(accountNumber);
        if (!exists) {
            return new AccountVerificationResponse(false, "The account number does not exist in the system.");
        }

        return new AccountVerificationResponse(true, "The account number is correct.");
    }
}
