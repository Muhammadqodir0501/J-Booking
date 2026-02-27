package com.example.jbooking.dto.response.jbank;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionResponseDto {
    private UUID id;              // Это bank_transaction_id
    private UUID referenceId;
    private String status;        // "CREATED"
    private BigDecimal amount;
    private String currency;
    private String createdAt;
}