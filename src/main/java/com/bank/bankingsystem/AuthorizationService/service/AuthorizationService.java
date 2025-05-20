package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.model.AuthorizationEntity;
import com.bank.bankingsystem.AuthorizationService.repository.AuthorizationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthorizationService {
    private final AuthorizationRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 15; // in minutes

    public AuthorizationService(AuthorizationRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(AuthorizationDTO dto) {
        AuthorizationEntity user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (isAccountLocked(user)) {
            throw new UnauthorizedException("Account is temporarily locked. Try again later.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setLockTime(LocalDateTime.now());
            }

            repository.save(user);

            throw new UnauthorizedException("Invalid username or password");
        }

        // Token sesji
        return "mock-token";
    }

    private boolean isAccountLocked(AuthorizationEntity user) {
        if (user.getLockTime() == null) return false;

        LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_TIME_DURATION);
        if (LocalDateTime.now().isBefore(unlockTime)) {
            return true;
        } else {
            user.setLockTime(null);
            user.setFailedLoginAttempts(0);
            repository.save(user);
            return false;
        }
    }
}
