package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.response.jbank.TransactionResponseDto;
import com.example.jbooking.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    @Transactional
    Payment initiatePayment(UUID bookingId, BigDecimal amount, String senderToken);

    @Transactional
    void handleBankWebhook(TransactionResponseDto webhookPayload);
}
