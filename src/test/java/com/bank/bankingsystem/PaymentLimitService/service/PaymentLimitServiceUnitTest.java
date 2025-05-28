package com.bank.bankingsystem.PaymentLimitService.service;

import com.bank.bankingsystem.PaymentLimitService.dto.LimitResponse;
import com.bank.bankingsystem.PaymentLimitService.dto.SetLimitRequest;
import com.bank.bankingsystem.PaymentLimitService.model.PaymentLimitEntity;
import com.bank.bankingsystem.PaymentLimitService.repository.PaymentLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentLimitServiceUnitTest {

    private PaymentLimitRepository paymentLimitRepository;
    private PaymentLimitService paymentLimitService;

    @BeforeEach
    void setUp() {
        paymentLimitRepository = mock(PaymentLimitRepository.class);
        paymentLimitService = new PaymentLimitService(paymentLimitRepository);
    }

    @Test
    void shouldSetNewLimitIfNotExists() {
        SetLimitRequest request = new SetLimitRequest();
        request.setCustomerId(1L);
        request.setDailyLimit(500.0);
        request.setMonthlyLimit(10000.0);
        request.setCurrency("PLN");

        when(paymentLimitRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        paymentLimitService.setLimit(request);

        verify(paymentLimitRepository, times(1)).save(any(PaymentLimitEntity.class));
    }

    @Test
    void shouldUpdateLimitIfExists() {
        PaymentLimitEntity existing = new PaymentLimitEntity();
        existing.setCustomerId(1L);

        SetLimitRequest request = new SetLimitRequest();
        request.setCustomerId(1L);
        request.setDailyLimit(1000.0);
        request.setMonthlyLimit(20000.0);
        request.setCurrency("USD");

        when(paymentLimitRepository.findByCustomerId(1L)).thenReturn(Optional.of(existing));

        paymentLimitService.setLimit(request);

        verify(paymentLimitRepository, times(1)).save(existing);
        assertEquals(1000.0, existing.getDailyLimit());
        assertEquals(20000.0, existing.getMonthlyLimit());
        assertEquals("USD", existing.getCurrency());
    }

    @Test
    void shouldReturnLimitResponse() {
        PaymentLimitEntity limit = new PaymentLimitEntity();
        limit.setDailyLimit(800.0);
        limit.setMonthlyLimit(16000.0);
        limit.setCurrency("EUR");

        when(paymentLimitRepository.findByCustomerId(1L)).thenReturn(Optional.of(limit));

        LimitResponse response = paymentLimitService.getLimitForCustomer(1L);

        assertEquals(800.0, response.getDailyLimit());
        assertEquals(16000.0, response.getMonthlyLimit());
        assertEquals("EUR", response.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenLimitNotFound() {
        when(paymentLimitRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> paymentLimitService.getLimitForCustomer(2L));

        assertEquals("No limit set for customer", exception.getMessage());
    }
}
