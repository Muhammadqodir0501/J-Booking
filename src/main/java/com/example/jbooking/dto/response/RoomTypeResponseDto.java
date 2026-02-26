package com.example.jbooking.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RoomTypeResponseDto {
    private UUID id;
    private String name;
    private String description;
    private int capacityAdults;
    private int capacityChildren;
    private String hotelName;
}
