package com.example.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RoomUpdateRequestDto {

    @NotNull(message = "ID комнаты обязателен")
    private UUID id;

    @NotNull(message = "ID типа комнаты обязателен")
    private UUID roomTypeId;

    @NotNull(message = "Номер комнаты обязателен")
    private String roomNumber;

    @NotNull
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;
}
