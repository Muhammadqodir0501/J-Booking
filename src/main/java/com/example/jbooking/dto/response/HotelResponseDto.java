package com.example.jbooking.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class HotelResponseDto {
    private UUID id;
    private UUID adminId;
    private String name;
    private String description;
    private String brand;
    private String city;
    private float rating;
    private List<String> amenities;
}