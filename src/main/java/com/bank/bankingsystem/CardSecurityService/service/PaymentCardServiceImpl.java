package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.AuthorizationService.service.UserService;
import com.bank.bankingsystem.CardSecurityService.dto.PaymentCardDTO;
import com.bank.bankingsystem.CardSecurityService.repository.PaymentCardRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository cardRepository;
    private final UserService userService;

    public PaymentCardServiceImpl(PaymentCardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    @Override
    public PaymentCardDTO getCardDetailsByUsername(String username) {
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var card = cardRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        return PaymentCardDTO.builder()
                .cardNumber(card.getCardNumber())
                .expiryDate(card.getExpiryDate())
                .cardHolderName(card.getCardHolderName())
                .build();
    }
}
