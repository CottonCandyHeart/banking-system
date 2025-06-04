package com.bank.bankingsystem.CardSecurityService.controller;

import com.bank.bankingsystem.CardSecurityService.dto.PaymentCardDTO;
import com.bank.bankingsystem.CardSecurityService.service.CardSecurityService;
import com.bank.bankingsystem.CardSecurityService.service.PaymentCardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardSecurityService cardSecurityService;
    private final PaymentCardService paymentCardService;

    public CardController(CardSecurityService cardSecurityService,
                          PaymentCardService paymentCardService) {
        this.cardSecurityService = cardSecurityService;
        this.paymentCardService = paymentCardService;
    }

    @GetMapping("/details")
    public PaymentCardDTO getCardDetails(@RequestParam String username) {
        if (!cardSecurityService.isAccessAuthorized(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Re-authentication required");
        }

        return paymentCardService.getCardDetailsByUsername(username);
    }
}

