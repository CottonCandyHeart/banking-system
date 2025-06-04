package com.bank.bankingsystem.CardSecurityService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardAccessRequestDTO {
    private String password;
}
