package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorizationDTO dto) {
        return ResponseEntity.ok(authorizationService.authenticate(dto));
    }
}
