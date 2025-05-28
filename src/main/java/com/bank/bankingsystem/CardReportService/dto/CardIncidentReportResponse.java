package com.bank.bankingsystem.CardReportService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardIncidentReportResponse {
    private boolean success;
    private String message;
}
