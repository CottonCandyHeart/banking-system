package com.bank.bankingsystem.CardReportService.repository;

import com.bank.bankingsystem.CardReportService.model.CardIncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardIncidentRepository extends JpaRepository<CardIncidentEntity, Long> {
    List<CardIncidentEntity> findByCustomerId(Long customerId);
}
