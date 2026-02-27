package com.example.jbooking.client;

import com.example.jbooking.dto.request.jnotification.NotificationSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class JNotificationClient {

    private final RestClient restClient;

    @Value("${jnotification.login}")
    private String login;

    @Value("${jnotification.password}")
    private String password;

    public JNotificationClient(@Value("${jnotification.url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl(baseUrl)
                .build();
    }

    @Async
    public void sendEmail(String email, String message) {
        log.info("Попытка отправки email на адрес: {}", email);

        NotificationSendRequest request = NotificationSendRequest.builder()
                .receiver(NotificationSendRequest.Receiver.builder().email(email).build())
                .type("EMAIL")
                .text(message)
                .build();

        try {
            String response = restClient.post()
                    .uri("/api/notifications/sending")
                    .headers(headers -> {
                        headers.setBasicAuth(login, password);
                        headers.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(request)
                    .retrieve()
                    .body(String.class);

            log.info("Уведомление успешно отправлено в J-Notification: {}", response);

        } catch (Exception e) {
            log.error("Критическая ошибка отправки уведомления для {}. Причина: {}", email, e.getMessage());
        }
    }
}