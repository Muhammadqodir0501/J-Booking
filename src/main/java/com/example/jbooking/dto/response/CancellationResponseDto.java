package com.example.jbooking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CancellationResponseDto {
    private UUID bookingId;
    private String status;
    private BigDecimal fine;
    private String message;

}
