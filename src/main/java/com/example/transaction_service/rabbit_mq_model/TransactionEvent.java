package com.example.transaction_service.rabbit_mq_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent implements Serializable {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String accountNumber;
    @JsonProperty
    private Long customerId;
    @JsonProperty
    private AccountType accountType;
    @JsonProperty
    private AccountStatus status;
    @JsonProperty
    private BigDecimal balance;
    @JsonProperty
    private BigDecimal overdraftLimit;
    @JsonProperty
    private String currency;
    @Override
    public String toString() {
        return "TransactionEvent{" +
                "accountId='" + id + '\'' +
                "accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId +
                ", accountType=" + accountType +
                ", currency='" + currency + '\'' +
                '}';
    }
}