package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
import com.bank.bankingsystem.AuthorizationService.service.UserService;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessRequestDTO;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessResponseDTO;
import com.bank.bankingsystem.CardSecurityService.model.CardSecuritySessionEntity;
import com.bank.bankingsystem.CardSecurityService.repository.CardSecuritySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardSecurityServiceImpl implements CardSecurityService {

    private final CardSecuritySessionRepository sessionRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CardSecurityServiceImpl(CardSecuritySessionRepository sessionRepository,
                                   UserService userService,
                                   PasswordEncoder passwordEncoder) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CardAccessResponseDTO authorize(String username, CardAccessRequestDTO request) {
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (matches) {
            sessionRepository.save(new CardSecuritySessionEntity(user.getId(), LocalDateTime.now()));
            return new CardAccessResponseDTO(true, "Access granted.");
        }

        return new CardAccessResponseDTO(false, "Invalid password.");
    }

    @Override
    public boolean isAccessAuthorized(String username) {
        return userService.findByUsername(username)
                .flatMap(user -> sessionRepository.findTopByUserIdOrderByAuthorizedAtDesc(user.getId()))
                .map(session -> session.getAuthorizedAt().isAfter(LocalDateTime.now().minusMinutes(5)))
                .orElse(false);
    }
}

