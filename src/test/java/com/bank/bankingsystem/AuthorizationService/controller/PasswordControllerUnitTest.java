package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.ChangePasswordRequest;
import com.bank.bankingsystem.AuthorizationService.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class PasswordControllerUnitTest {
    private PasswordService passwordService;
    private PasswordController passwordController;

    @BeforeEach
    void setUp() {
        passwordService = mock(PasswordService.class);
        passwordController = new PasswordController(passwordService);
    }

    @Test
    void shouldReturnSuccessMessage() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldpassword");
        request.setNewPassword("newpassword");

        ResponseEntity<String> response = passwordController.changePassword(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password changed successfully.", response.getBody());
        verify(passwordService, times(1)).changePassword(request);
    }
}
