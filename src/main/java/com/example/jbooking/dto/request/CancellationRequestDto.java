package com.example.jbooking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CancellationRequestDto {

    @NotNull(message = "ID типа комнаты обязателен")
    private UUID roomTypeId;

    @NotNull(message = "Укажите количество часов до заезда")
    @Min(value = 0, message = "Часы не могут быть отрицательными")
    private int hoursBeforeCheckIn;

    @NotNull(message = "Укажите процент штрафа")
    @Min(value = 0, message = "Штраф минимум 0%")
    @Max(value = 10, message = "Штраф максимум 10%")
    private int penaltyPercentage;

}
