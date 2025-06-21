package com.example.transaction_service.dto;

import com.example.transaction_service.entity.TransactionStatus;
import com.example.transaction_service.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponsDTO {

    private Long id;
    private String transactionReference;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
    private int initiatedBy;

    public TransactionResponsDTO() {}

    public TransactionResponsDTO(Long id, Long fromAccountId, Long toAccountId, BigDecimal amount,
                                 TransactionType type, TransactionStatus status, String description,
                                 LocalDateTime createdAt, int initiatedBy) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.initiatedBy = initiatedBy;
    }

    // Getters and Setters
}
