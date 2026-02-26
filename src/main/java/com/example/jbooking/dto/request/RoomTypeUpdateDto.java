package com.example.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RoomTypeUpdateDto {

    @NotNull(message = "ID типа комнаты обязателен")
    private UUID id;

    @NotNull(message = "Название типа обязательно")
    private String name;
    private String description;
    private int capacityAdults;
    private int capacityChildren;
}
