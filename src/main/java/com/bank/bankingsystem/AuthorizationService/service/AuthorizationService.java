package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.model.AuthorizationEntity;
import com.bank.bankingsystem.AuthorizationService.repository.AuthorizationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final AuthorizationRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationService(AuthorizationRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(AuthorizationDTO dto) {
        AuthorizationEntity user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        // Token sesji)
        return "mock-token";
    }
}
