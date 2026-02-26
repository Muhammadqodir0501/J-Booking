package com.example.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class HotelRequestDto {

    @NotBlank(message = "Название отеля обязательно")
    private String name;
    private String description;
    private String brand;
    private String country;
    private String city;
    private String address;
    private List<String> amenities;
}
