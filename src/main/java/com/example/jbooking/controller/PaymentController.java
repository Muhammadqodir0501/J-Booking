package com.example.jbooking.controller;

import com.example.jbooking.dto.response.jbank.TransactionResponseDto;
import com.example.jbooking.service.abstaction.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleBankWebhook(@RequestBody TransactionResponseDto webhookPayload) {
        paymentService.handleBankWebhook(webhookPayload);
        return ResponseEntity.ok("Webhook processed");
    }
}