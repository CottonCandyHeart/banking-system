package com.bank.bankingsystem.CardSecurityService.controller;

import com.bank.bankingsystem.CardSecurityService.dto.PaymentCardDTO;
import com.bank.bankingsystem.CardSecurityService.service.CardSecurityService;
import com.bank.bankingsystem.CardSecurityService.service.PaymentCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardControllerUnitTest {
    private CardSecurityService cardSecurityService;
    private PaymentCardService paymentCardService;
    private CardController cardController;

    @BeforeEach
    public void setUp() {
        cardSecurityService = mock(CardSecurityService.class);
        paymentCardService = mock(PaymentCardService.class);
        cardController = new CardController(cardSecurityService, paymentCardService);
    }

    @Test
    public void shouldReturnCardDetailsWhenUserIsAuthorized() {
        String username = "john_doe";
        PaymentCardDTO mockCard = new PaymentCardDTO("1234-5678-9012-3456", "12/26", "John Doe");

        when(cardSecurityService.isAccessAuthorized(username)).thenReturn(true);
        when(paymentCardService.getCardDetailsByUsername(username)).thenReturn(mockCard);

        PaymentCardDTO result = cardController.getCardDetails(username);

        assertNotNull(result);
        assertEquals("1234-5678-9012-3456", result.getCardNumber());
        assertEquals("12/26", result.getExpiryDate());
        assertEquals("John Doe", result.getCardHolderName());

        verify(cardSecurityService).isAccessAuthorized(username);
        verify(paymentCardService).getCardDetailsByUsername(username);
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotAuthorized() {
        String username = "unauthorized_user";
        when(cardSecurityService.isAccessAuthorized(username)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cardController.getCardDetails(username));

        assertEquals(403, exception.getStatusCode().value());
        assertEquals("403 FORBIDDEN \"Re-authentication required\"", exception.getMessage());

        verify(cardSecurityService).isAccessAuthorized(username);
        verify(paymentCardService, never()).getCardDetailsByUsername(any());
    }
}
