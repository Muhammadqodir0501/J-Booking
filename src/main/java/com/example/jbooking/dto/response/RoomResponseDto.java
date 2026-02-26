package com.example.jbooking.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RoomResponseDto {
    private UUID id;
    private String roomNumber;
    private BigDecimal price;
    private boolean active;

    private String roomTypeName;
    private int capacityAdults;
}