package com.bank.bankingsystem.PaymentLimitService.service;

import com.bank.bankingsystem.PaymentLimitService.dto.LimitResponse;
import com.bank.bankingsystem.PaymentLimitService.dto.SetLimitRequest;
import com.bank.bankingsystem.PaymentLimitService.model.PaymentLimitEntity;
import com.bank.bankingsystem.PaymentLimitService.repository.PaymentLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentLimitService {
    private PaymentLimitRepository paymentLimitRepository;

    public PaymentLimitService(PaymentLimitRepository paymentLimitRepository){
        this.paymentLimitRepository = paymentLimitRepository;
    }

    public void setLimit(SetLimitRequest request) {
        Optional<PaymentLimitEntity> optional = paymentLimitRepository.findByCustomerId(request.getCustomerId());

        PaymentLimitEntity limit = optional.orElseGet(PaymentLimitEntity::new);
        limit.setCustomerId(request.getCustomerId());
        limit.setDailyLimit(request.getDailyLimit());
        limit.setMonthlyLimit(request.getMonthlyLimit());
        limit.setCurrency(request.getCurrency());

        paymentLimitRepository.save(limit);
    }

    public LimitResponse getLimitForCustomer(Long customerId) {
        PaymentLimitEntity limit = paymentLimitRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("No limit set for customer"));

        return new LimitResponse(limit.getDailyLimit(), limit.getMonthlyLimit(), limit.getCurrency());
    }
}
