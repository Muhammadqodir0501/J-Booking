package com.example.jbooking.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingResponseDto {
    private UUID id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private String status;

    private String roomNumber;
    private String roomTypeName;
    private UUID hotelId;
}