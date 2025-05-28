package com.bank.bankingsystem.CardReportService.controller;

import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportRequest;
import com.bank.bankingsystem.CardReportService.dto.CardIncidentReportResponse;
import com.bank.bankingsystem.CardReportService.service.CardIncidentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card-incident")
public class CardIncidentController {
    private CardIncidentService cardIncidentService;

    public CardIncidentController(CardIncidentService cardIncidentService){
        this.cardIncidentService = cardIncidentService;
    }

    @PostMapping("/report")
    public CardIncidentReportResponse reportIncident(@RequestBody CardIncidentReportRequest request) {
        return cardIncidentService.reportIncident(request);
    }
}
