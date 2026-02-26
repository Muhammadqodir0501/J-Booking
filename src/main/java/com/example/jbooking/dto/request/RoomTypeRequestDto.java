package com.example.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RoomTypeRequestDto {
    @NotNull(message = "ID отеля обязательно")
    private UUID hotelId;

    @NotNull(message = "Название типа обязательно")
    private String name;
    private String description;
    private Integer capacityAdults;
    private Integer capacityChildren;
}
