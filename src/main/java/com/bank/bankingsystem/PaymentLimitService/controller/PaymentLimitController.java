package com.bank.bankingsystem.PaymentLimitService.controller;

import com.bank.bankingsystem.PaymentLimitService.dto.LimitResponse;
import com.bank.bankingsystem.PaymentLimitService.dto.SetLimitRequest;
import com.bank.bankingsystem.PaymentLimitService.service.PaymentLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-limits")
public class PaymentLimitController {
    private final PaymentLimitService paymentLimitService;

    public PaymentLimitController(PaymentLimitService paymentLimitService) {
        this.paymentLimitService = paymentLimitService;
    }

    @PostMapping("/set")
    public void setLimit(@RequestBody SetLimitRequest request) {
        paymentLimitService.setLimit(request);
    }

    @GetMapping("/{customerId}")
    public LimitResponse getLimit(@PathVariable Long customerId) {
        return paymentLimitService.getLimitForCustomer(customerId);
    }
}
