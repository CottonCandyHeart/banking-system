package com.bank.bankingsystem.CardReportService.service;

import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportRequest;
import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportResponse;
import com.bank.bankingsystem.CardReportService.model.CardIncidentEntity;
import com.bank.bankingsystem.CardReportService.repository.CardIncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardIncidentServiceUnitTest {
    private CardIncidentService cardIncidentService;
    private CardIncidentRepository cardIncidentRepository;

    @BeforeEach
    void setUp() {
        cardIncidentRepository = mock(CardIncidentRepository.class);
        cardIncidentService = new CardIncidentService(cardIncidentRepository);
    }

    @Test
    void shouldSaveIncidentAndReturnSuccessResponse() {
        CardIncidentReportRequest request = new CardIncidentReportRequest();
        request.setCustomerId(1L);
        request.setCardNumber("1234567890123456");
        request.setIncidentType("STOLEN");
        request.setDescription("Card was stolen while traveling.");

        when(cardIncidentRepository.save(any(CardIncidentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CardIncidentReportResponse response = cardIncidentService.reportIncident(request);

        verify(cardIncidentRepository, times(1)).save(any(CardIncidentEntity.class));

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Incident reported successfully.", response.getMessage());
    }
}
