package com.example.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingRequestDto {

    @NotNull(message = "ID пользователя обязателен для теста")
    private UUID userId;

    @NotNull(message = "ID комнаты обязателен")
    private UUID roomId;

    @NotNull
    @FutureOrPresent(message = "Дата заезда должна быть в будущем")
    private LocalDate checkInDate;

    @NotNull
    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDate checkOutDate;

}