package com.bank.bankingsystem.CardSecurityService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCardDTO {
    private String cardNumber;
    private String expiryDate;
    private String cardHolderName;
}