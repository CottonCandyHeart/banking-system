package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.model.AuthorizationEntity;
import com.bank.bankingsystem.AuthorizationService.repository.AuthorizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationServiceUnitTest {
    private AuthorizationRepository authorizationRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp(){
        authorizationRepository = mock(AuthorizationRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authorizationService = new AuthorizationService(authorizationRepository, passwordEncoder);
    }

    @Test
    void shouldReturnTokenWhenWhenLogInSuccessful(){
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("password","hashed")).thenReturn(true);

        String result = authorizationService.authenticate(dto);

        assertEquals("mock-token", result);
    }

    @Test
    void shouldThrowUnauthorizedWhenPasswordDoesNotMatch() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("wrongpassword");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authorizationService.authenticate(dto));
    }

    @Test
    void shouldThrowUnauthorizedWhenUsernameDoesNotExist() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("wronguser");
        dto.setPassword("password");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");

        when(authorizationRepository.findByUsername("invalidUsername")).thenReturn(Optional.empty());
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authenticate(dto);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }
}
