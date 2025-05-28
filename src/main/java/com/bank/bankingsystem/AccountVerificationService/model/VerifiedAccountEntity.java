package com.bank.bankingsystem.AccountVerificationService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VerifiedAccountEntity {
    @Id
    private String accountNumber;
    private boolean valid;

}
