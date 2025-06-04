package com.bank.bankingsystem.CardSecurityService.controller;

import com.bank.bankingsystem.CardSecurityService.dto.CardAccessRequestDTO;
import com.bank.bankingsystem.CardSecurityService.dto.CardAccessResponseDTO;
import com.bank.bankingsystem.CardSecurityService.service.CardSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardSecurityControllerUnitTest {
    private CardSecurityService cardSecurityService;
    private CardSecurityController cardSecurityController;

    @BeforeEach
    public void setUp() {
        cardSecurityService = mock(CardSecurityService.class);
        cardSecurityController = new CardSecurityController(cardSecurityService);
    }

    @Test
    public void shouldAuthenticateUserWhenCorrectPasswordProvided() {
        String username = "john_doe";
        CardAccessRequestDTO request = new CardAccessRequestDTO("secret123");
        CardAccessResponseDTO expectedResponse = new CardAccessResponseDTO(true, "Access granted.");

        when(cardSecurityService.authorize(username, request)).thenReturn(expectedResponse);

        CardAccessResponseDTO result = cardSecurityController.authenticate(username, request);

        assertNotNull(result);
        assertTrue(result.isAuthorized());
        assertEquals("Access granted.", result.getMessage());

        verify(cardSecurityService).authorize(username, request);
    }

    @Test
    public void shouldReturnTrueWhenUserIsAuthorized() {
        String username = "authorized_user";
        when(cardSecurityService.isAccessAuthorized(username)).thenReturn(true);

        boolean result = cardSecurityController.isAuthorized(username);

        assertTrue(result);
        verify(cardSecurityService).isAccessAuthorized(username);
    }

    @Test
    public void shouldReturnFalseWhenUserIsNotAuthorized() {
        String username = "unauthorized_user";
        when(cardSecurityService.isAccessAuthorized(username)).thenReturn(false);

        boolean result = cardSecurityController.isAuthorized(username);

        assertFalse(result);
        verify(cardSecurityService).isAccessAuthorized(username);
    }
}
