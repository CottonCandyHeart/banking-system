package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.ChangePasswordRequest;
import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
import com.bank.bankingsystem.AuthorizationService.util.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PasswordServiceUnitTest {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private PasswordService passwordService;

    private SecurityContext securityContext;
    private Authentication authentication;

    private MockedStatic<SecurityContextHolder> mockedContextHolder;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        passwordService = new PasswordService(userService, passwordEncoder);

        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        mockedContextHolder = mockStatic(SecurityContextHolder.class);
        mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    private void mockSecurityContext(String username) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @AfterEach
    void tearDown() {
        mockedContextHolder.close();
    }

    @Test
    void shouldChangePasswordWhenRequestIsValid() {
        String username = "testuser";
        String oldPassword = "oldPassword123!";
        String newPassword = "newPassword123!";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setConfirmNewPassword(newPassword);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedOldPassword123");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("hashedOldPassword123");

        passwordService.changePassword(request);

        assertEquals("hashedOldPassword123", user.getPassword());
        verify(userService).save(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword123!");
        request.setNewPassword("newPassword123!");
        request.setConfirmNewPassword("Different123!");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordService.changePassword(request)
        );

        assertEquals("New passwords do not match.", ex.getMessage());
        verifyNoInteractions(userService);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "missinguser";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword123!");
        request.setNewPassword("newPassword123!");
        request.setConfirmNewPassword("newPassword123!");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                passwordService.changePassword(request)
        );

        assertEquals("User not found.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        String username = "testuser";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongOldPassword123!");
        request.setNewPassword("newPassword123!");
        request.setConfirmNewPassword("newPassword123!");

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedPassword123!");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword123!", "hashedPassword123!")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordService.changePassword(request)
        );

        assertEquals("Current password is incorrect.", ex.getMessage());
        verify(userService, never()).save(any());
    }

    @Test
    void shouldUsePrincipalToStringWhenNotUserDetails() {
        String username = "test";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword123!");
        request.setNewPassword("newPassword123!");
        request.setConfirmNewPassword("newPassword123!");

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedOldPassword123!");

        when(authentication.getPrincipal()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword123!", "hashedOldPassword123!")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123!")).thenReturn("hashedNewPassword123!");

        passwordService.changePassword(request);

        assertEquals("hashedNewPassword123!", user.getPassword());
        verify(userService).save(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWeak() {
        String username = "testuser";
        String oldPassword = "oldPassword123!";
        String weakPassword = "newpassword123";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(oldPassword);
        request.setNewPassword(weakPassword);
        request.setConfirmNewPassword(weakPassword);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedOldPassword123!");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

        try (MockedStatic<PasswordValidator> validatorMock = mockStatic(PasswordValidator.class)) {
            validatorMock.when(() -> PasswordValidator.isStrong(weakPassword)).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    passwordService.changePassword(request)
            );

            assertEquals("Password is too weak.", ex.getMessage());
            verify(userService, never()).save(any());
        }
    }


}
