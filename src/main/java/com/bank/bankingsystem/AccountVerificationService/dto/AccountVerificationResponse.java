package com.bank.bankingsystem.AccountVerificationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountVerificationResponse {
    private boolean valid;
    private String message;

    public AccountVerificationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
}
