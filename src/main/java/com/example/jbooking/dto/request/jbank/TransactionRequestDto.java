package com.example.jbooking.dto.request.jbank;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionRequestDto {
    private UUID referenceId;     // Payment.getId()
    private String type;          // "P2P"
    private BigDecimal amount;
    private String currency;      // "UZS"
    private UUID merchantId;      //ID мерчанта в J-Bank
    private String senderName;
    private String senderToken;   // Токен карты клиента
    private String receiverName;
    private String receiverToken; // Токен карты отеля
}