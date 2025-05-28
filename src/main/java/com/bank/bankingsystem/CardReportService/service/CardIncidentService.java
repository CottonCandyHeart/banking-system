package com.bank.bankingsystem.CardReportService.service;

import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportRequest;
import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportResponse;
import com.bank.bankingsystem.CardReportService.model.CardIncidentEntity;
import com.bank.bankingsystem.CardReportService.repository.CardIncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardIncidentService {
    private final CardIncidentRepository cardIncidentRepository;

    public CardIncidentService(CardIncidentRepository cardIncidentRepository) {
        this.cardIncidentRepository = cardIncidentRepository;
    }

    public CardIncidentReportResponse reportIncident(CardIncidentReportRequest request) {
        CardIncidentEntity incident = new CardIncidentEntity();
        incident.setCustomerId(request.getCustomerId());
        incident.setCardNumber(request.getCardNumber());
        incident.setIncidentType(request.getIncidentType());
        incident.setDescription(request.getDescription());
        incident.setReportedAt(LocalDateTime.now());

        cardIncidentRepository.save(incident);

        return new CardIncidentReportResponse(true, "Incident reported successfully.");
    }
}
