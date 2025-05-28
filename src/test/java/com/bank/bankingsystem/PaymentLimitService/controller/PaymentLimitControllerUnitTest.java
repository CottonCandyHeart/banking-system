package com.bank.bankingsystem.PaymentLimitService.controller;

import com.bank.bankingsystem.PaymentLimitService.dto.LimitResponse;
import com.bank.bankingsystem.PaymentLimitService.dto.SetLimitRequest;
import com.bank.bankingsystem.PaymentLimitService.service.PaymentLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentLimitControllerUnitTest {

    private MockMvc mockMvc;
    private PaymentLimitService paymentLimitService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        paymentLimitService = mock(PaymentLimitService.class);
        PaymentLimitController controller = new PaymentLimitController(paymentLimitService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCallServiceToSetLimit() throws Exception {
        SetLimitRequest request = new SetLimitRequest();
        request.setCustomerId(1L);
        request.setDailyLimit(1000.0);
        request.setMonthlyLimit(30000.0);
        request.setCurrency("PLN");

        mockMvc.perform(post("/api/payment-limits/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(paymentLimitService, times(1)).setLimit(any(SetLimitRequest.class));
    }

    @Test
    void shouldReturnLimitResponse() throws Exception {
        Long customerId = 1L;
        LimitResponse response = new LimitResponse(1000.0, 30000.0, "PLN");

        when(paymentLimitService.getLimitForCustomer(customerId)).thenReturn(response);

        mockMvc.perform(get("/api/payment-limits/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyLimit").value(1000.0))
                .andExpect(jsonPath("$.monthlyLimit").value(30000.0))
                .andExpect(jsonPath("$.currency").value("PLN"));
    }
}
