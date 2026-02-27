package com.example.jbooking.dto.request.jnotification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationSendRequest {
    private Receiver receiver;
    private String type;
    private String text;

    @Data
    @Builder
    public static class Receiver {
        private String phone;
        private String email;
        private String firebaseToken;
    }
}