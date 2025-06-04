package com.bank.bankingsystem.CardSecurityService.controller;

import com.bank.bankingsystem.CardSecurityService.dto.CardAccessRequestDTO;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessResponseDTO;
import com.bank.bankingsystem.CardSecurityService.service.CardSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card/security")
public class CardSecurityController {

    private final CardSecurityService cardSecurityService;

    @Autowired
    public CardSecurityController(CardSecurityService cardSecurityService) {
        this.cardSecurityService = cardSecurityService;
    }

    @PostMapping("/authenticate")
    public CardAccessResponseDTO authenticate(@RequestParam String username,
                                              @RequestBody CardAccessRequestDTO request) {
        return cardSecurityService.authorize(username, request);
    }

    @GetMapping("/verify")
    public boolean isAuthorized(@RequestParam String username) {
        return cardSecurityService.isAccessAuthorized(username);
    }
}
