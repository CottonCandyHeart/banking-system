package com.bank.bankingsystem.PaymentLimitService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetLimitRequest {
    private Long customerId;
    private Double dailyLimit;
    private Double monthlyLimit;
    private String currency;
}
