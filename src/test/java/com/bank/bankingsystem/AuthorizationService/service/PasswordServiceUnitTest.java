package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.ChangePasswordRequest;
import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
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
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setConfirmNewPassword(newPassword);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedoldpassword");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("hashednewpassword");

        passwordService.changePassword(request);

        assertEquals("hashednewpassword", user.getPassword());
        verify(userService).save(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldpassword");
        request.setNewPassword("newpassword");
        request.setConfirmNewPassword("different");

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
        request.setCurrentPassword("oldpassword");
        request.setNewPassword("newpassword");
        request.setConfirmNewPassword("newpassword");

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
        request.setCurrentPassword("wrongoldpassword");
        request.setNewPassword("newpassword");
        request.setConfirmNewPassword("newpassword");

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedpassword");

        mockSecurityContext(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongoldpassword", "hashedpassword")).thenReturn(false);

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
        request.setCurrentPassword("oldpassword");
        request.setNewPassword("newpassword");
        request.setConfirmNewPassword("newpassword");

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashedoldpassword");

        when(authentication.getPrincipal()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpassword", "hashedoldpassword")).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("hashednewpassword");

        passwordService.changePassword(request);

        assertEquals("hashednewpassword", user.getPassword());
        verify(userService).save(user);
    }
}
