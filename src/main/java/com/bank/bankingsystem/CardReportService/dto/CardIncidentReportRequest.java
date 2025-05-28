package com.bank.bankingsystem.CardReportService.dto;

import lombok.Data;

@Data
public class CardIncidentReportRequest {
    private Long customerId;
    private String cardNumber;
    private String incidentType;
    private String description;
}
