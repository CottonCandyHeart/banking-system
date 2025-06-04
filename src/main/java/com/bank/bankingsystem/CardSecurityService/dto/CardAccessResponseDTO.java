package com.bank.bankingsystem.CardSecurityService.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardAccessResponseDTO {
    private boolean authorized;
    private String message;

    public CardAccessResponseDTO(boolean authorized, String message) {
        this.authorized = authorized;
        this.message = message;
    }
}
