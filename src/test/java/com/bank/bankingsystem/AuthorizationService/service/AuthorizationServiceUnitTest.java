package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuditLogService.repository.LoginLogRepository;
import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.model.AuthorizationEntity;
import com.bank.bankingsystem.AuthorizationService.repository.AuthorizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorizationServiceUnitTest {
    private AuthorizationRepository authorizationRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorizationService authorizationService;
    private LoginLogRepository loginLogRepository;

    @BeforeEach
    void setUp(){
        authorizationRepository = mock(AuthorizationRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        loginLogRepository = mock(LoginLogRepository.class);

        authorizationService = new AuthorizationService(authorizationRepository, passwordEncoder, loginLogRepository);
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

    @Test
    void shouldThrowUnauthorizedWhenAccountIsLocked() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");
        entity.setLockTime(LocalDateTime.now());

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authenticate(dto);
        });

        assertEquals("Account is temporarily locked. Try again later.", exception.getMessage());
    }

    @Test
    void shouldResetLockWhenLockTimeExpired() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");
        entity.setFailedLoginAttempts(5);
        entity.setLockTime(LocalDateTime.now().minusMinutes(20));

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        String result = authorizationService.authenticate(dto);

        assertEquals("mock-token", result);
        assertEquals(0, entity.getFailedLoginAttempts());
        assertNull(entity.getLockTime());
    }

    @Test
    void shouldIncrementFailedLoginAttemptsWhenPasswordIncorrect() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("wrongpassword");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");
        entity.setFailedLoginAttempts(2);
        entity.setLockTime(null);

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authenticate(dto);
        });

        assertEquals(3, entity.getFailedLoginAttempts());
    }

    @Test
    void shouldLockAccountAfterMaxFailedAttempts() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("wrongpassword");

        AuthorizationEntity entity = new AuthorizationEntity();
        entity.setUsername("user");
        entity.setPasswordHash("hashed");
        entity.setFailedLoginAttempts(4);
        entity.setLockTime(null);

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authenticate(dto);
        });

        assertEquals(5, entity.getFailedLoginAttempts());
        assertNotNull(entity.getLockTime());
    }

    //-------------------------------------------------------------
    @Test
    void shouldLogFailedAttemptWhenUsernameDoesNotExist() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("wrongusername");
        dto.setPassword("password");

        when(authorizationRepository.findByUsername("wrongusername")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authorizationService.authenticate(dto));

        verify(loginLogRepository).save(argThat(log ->
                log.getUserId() == null &&
                        !log.isSuccess()
        ));
    }

    @Test
    void shouldLogFailedAttemptWhenPasswordIsWrong() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("wrongpassword");

        AuthorizationEntity user = new AuthorizationEntity();
        user.setId(1L);
        user.setUsername("user");
        user.setPasswordHash("hashed");
        user.setFailedLoginAttempts(0);

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authorizationService.authenticate(dto));

        verify(loginLogRepository).save(argThat(log ->
                Objects.equals(log.getUserId(), user.getId()) && !log.isSuccess()
        ));
    }

    @Test
    void shouldLogSuccessfulAttempt() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        AuthorizationEntity user = new AuthorizationEntity();
        user.setId(1L);
        user.setUsername("user");
        user.setPasswordHash("hashed");

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        String token = authorizationService.authenticate(dto);

        assertEquals("mock-token", token);
        verify(loginLogRepository).save(argThat(log ->
                log.getUserId().equals(1L) &&
                        log.isSuccess()
        ));
    }

    @Test
    void shouldUnlockAccountWhenLockTimeExpired() {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        AuthorizationEntity user = new AuthorizationEntity();
        user.setId(1L);
        user.setUsername("user");
        user.setPasswordHash("hashed");
        user.setFailedLoginAttempts(5);
        user.setLockTime(LocalDateTime.now().minusMinutes(30));

        when(authorizationRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        String token = authorizationService.authenticate(dto);

        assertEquals("mock-token", token);
        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockTime());
    }



}
