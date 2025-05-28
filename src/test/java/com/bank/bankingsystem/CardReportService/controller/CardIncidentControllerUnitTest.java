package com.bank.bankingsystem.CardReportService.controller;

import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportRequest;
import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportResponse;
import com.bank.bankingsystem.CardReportService.service.CardIncidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CardIncidentControllerUnitTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CardIncidentService cardIncidentService;

    @BeforeEach
    void setUp() {
        cardIncidentService = mock(CardIncidentService.class);
        CardIncidentController controller = new CardIncidentController(cardIncidentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnSuccessResponseWhenIncidentReported() throws Exception {
        CardIncidentReportRequest request = new CardIncidentReportRequest();
        request.setCustomerId(1L);
        request.setCardNumber("1234567890123456");
        request.setIncidentType("LOST");
        request.setDescription("Left wallet in a taxi.");

        CardIncidentReportResponse response = new CardIncidentReportResponse(true, "Incident reported successfully.");

        when(cardIncidentService.reportIncident(any(CardIncidentReportRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/card-incident/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Incident reported successfully."));

        verify(cardIncidentService, times(1)).reportIncident(any(CardIncidentReportRequest.class));
    }
}
