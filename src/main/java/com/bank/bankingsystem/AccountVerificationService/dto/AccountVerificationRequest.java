package com.bank.bankingsystem.AccountVerificationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountVerificationRequest {
    private String accountNumber;

    public AccountVerificationRequest(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
