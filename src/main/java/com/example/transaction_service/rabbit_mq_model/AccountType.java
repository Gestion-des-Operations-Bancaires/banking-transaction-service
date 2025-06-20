package com.example.transaction_service.rabbit_mq_model;

import lombok.Getter;

@Getter
public enum AccountType {
    CURRENT("Compte Courant"),
    SAVINGS("Compte Épargne"), 
    PROFESSIONAL("Compte Professionnel"),
    JOINT("Compte Joint");
    
    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

}