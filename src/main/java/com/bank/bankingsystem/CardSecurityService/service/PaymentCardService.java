package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.CardSecurityService.dto.PaymentCardDTO;

public interface PaymentCardService {
    PaymentCardDTO getCardDetailsByUsername(String username);
}