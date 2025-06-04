package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.CardSecurityService.dto.CardAccessRequestDTO;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessResponseDTO;

public interface CardSecurityService {
    CardAccessResponseDTO authorize(String username, CardAccessRequestDTO request);
    boolean isAccessAuthorized(String username);
}
