package com.bank.bankingsystem.CardSecurityService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String cardNumber;
    private String expiryDate;
    private String cardHolderName;
    private String maskedCvc;
}