package com.bank.bankingsystem.PaymentLimitService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LimitResponse {
    private Double dailyLimit;
    private Double monthlyLimit;
    private String currency;
}
