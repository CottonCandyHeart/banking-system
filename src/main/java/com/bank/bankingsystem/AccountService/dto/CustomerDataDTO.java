package com.bank.bankingsystem.AccountService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDataDTO {
    private Long id;
    private String fullName;
    private String email;
    private String address;
    private String phoneNumber;

    public CustomerDataDTO(Long id, String fullName, String email, String address, String phoneNumber){
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
