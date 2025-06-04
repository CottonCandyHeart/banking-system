package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
import com.bank.bankingsystem.AuthorizationService.service.UserService;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessRequestDTO;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessResponseDTO;
import com.bank.bankingsystem.CardSecurityService.model.CardSecuritySessionEntity;
import com.bank.bankingsystem.CardSecurityService.repository.CardSecuritySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardSecurityServiceImplUnitTest {

    private CardSecuritySessionRepository sessionRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private CardSecurityServiceImpl cardSecurityService;

    @BeforeEach
    public void setUp() {
        sessionRepository = mock(CardSecuritySessionRepository.class);
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        cardSecurityService = new CardSecurityServiceImpl(sessionRepository, userService, passwordEncoder);
    }

    @Test
    public void shouldGrantAccessWhenPasswordMatches() {
        String username = "john_doe";
        String rawPassword = "secret123";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(encodedPassword);

        CardAccessRequestDTO request = new CardAccessRequestDTO(rawPassword);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        CardAccessResponseDTO response = cardSecurityService.authorize(username, request);

        assertTrue(response.isAuthorized());
        assertEquals("Access granted.", response.getMessage());

        ArgumentCaptor<CardSecuritySessionEntity> captor = ArgumentCaptor.forClass(CardSecuritySessionEntity.class);
        verify(sessionRepository).save(captor.capture());

        CardSecuritySessionEntity savedSession = captor.getValue();
        assertEquals(1L, savedSession.getUserId());
        assertNotNull(savedSession.getAuthorizedAt());
    }

    @Test
    public void shouldDenyAccessWhenPasswordDoesNotMatch() {
        String username = "john_doe";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(encodedPassword);

        CardAccessRequestDTO request = new CardAccessRequestDTO(rawPassword);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        CardAccessResponseDTO response = cardSecurityService.authorize(username, request);

        assertFalse(response.isAuthorized());
        assertEquals("Invalid password.", response.getMessage());

        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void shouldReturnTrueWhenSessionIsRecent() {
        String username = "john_doe";
        long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername(username);

        CardSecuritySessionEntity session = new CardSecuritySessionEntity(userId, LocalDateTime.now());

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionRepository.findTopByUserIdOrderByAuthorizedAtDesc(userId)).thenReturn(Optional.of(session));

        boolean authorized = cardSecurityService.isAccessAuthorized(username);

        assertTrue(authorized);
    }

    @Test
    public void shouldReturnFalseWhenNoRecentSession() {
        String username = "john_doe";
        long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        CardSecuritySessionEntity oldSession = new CardSecuritySessionEntity(userId, LocalDateTime.now().minusMinutes(10));

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionRepository.findTopByUserIdOrderByAuthorizedAtDesc(userId)).thenReturn(Optional.of(oldSession));

        boolean authorized = cardSecurityService.isAccessAuthorized(username);

        assertFalse(authorized);
    }

    @Test
    public void shouldReturnFalseWhenNoSessionExists() {
        String username = "john_doe";
        long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionRepository.findTopByUserIdOrderByAuthorizedAtDesc(userId)).thenReturn(Optional.empty());

        boolean authorized = cardSecurityService.isAccessAuthorized(username);

        assertFalse(authorized);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        String username = "ghost";
        CardAccessRequestDTO request = new CardAccessRequestDTO("irrelevant");

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cardSecurityService.authorize(username, request));

        assertEquals("User not found", ex.getMessage());
    }
}