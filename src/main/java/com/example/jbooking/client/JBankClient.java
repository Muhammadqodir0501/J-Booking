package com.example.jbooking.client;

import com.example.jbooking.dto.request.jbank.TransactionRequestDto;
import com.example.jbooking.dto.response.jbank.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class JBankClient {

    private final RestClient jBankRestClient;

    public TransactionResponseDto createTransaction(TransactionRequestDto request) {
        log.info("Отправка запроса на транзакцию в J-Bank для ReferenceID: {}", request.getReferenceId());

        try {
            return jBankRestClient.post()
                    .uri("/api/v1/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(TransactionResponseDto.class);

        } catch (Exception e) {
            log.error("Критическая ошибка при вызове J-Bank: {}", e.getMessage());
            throw new RuntimeException("Ошибка интеграции с банком: сервис временно недоступен");
        }
    }
}