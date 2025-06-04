package com.bank.bankingsystem.CardSecurityService.service;

import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
import com.bank.bankingsystem.AuthorizationService.service.UserService;
import com.bank.bankingsystem.CardSecurityService.dto.PaymentCardDTO;
import com.bank.bankingsystem.CardSecurityService.model.PaymentCardEntity;
import com.bank.bankingsystem.CardSecurityService.repository.PaymentCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentCardServiceImplUnitTest {

    private PaymentCardRepository cardRepository;
    private UserService userService;
    private PaymentCardServiceImpl paymentCardService;

    @BeforeEach
    public void setUp() {
        cardRepository = mock(PaymentCardRepository.class);
        userService = mock(UserService.class);
        paymentCardService = new PaymentCardServiceImpl(cardRepository, userService);
    }

    @Test
    public void shouldReturnCardDetailsWhenUserAndCardExist() {
        String username = "john_doe";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);

        PaymentCardEntity card = new PaymentCardEntity();
        card.setCardNumber("1234567890123456");
        card.setExpiryDate("12/26");
        card.setCardHolderName("John Doe");

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(cardRepository.findByUserId(user.getId())).thenReturn(Optional.of(card));

        PaymentCardDTO dto = paymentCardService.getCardDetailsByUsername(username);

        assertNotNull(dto);
        assertEquals("1234567890123456", dto.getCardNumber());
        assertEquals("12/26", dto.getExpiryDate());
        assertEquals("John Doe", dto.getCardHolderName());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        String username = "ghost";

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                paymentCardService.getCardDetailsByUsername(username));

        assertEquals("User not found", ex.getMessage());
        verify(cardRepository, never()).findByUserId(anyLong());
    }

    @Test
    public void shouldThrowExceptionWhenCardNotFound() {
        String username = "john_doe";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(cardRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                paymentCardService.getCardDetailsByUsername(username));

        assertEquals("Card not found", ex.getMessage());
    }
}
