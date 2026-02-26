package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.HotelRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface HotelService {

    @Transactional
    HotelResponseDto createHotel(HotelRequestDto request, UUID adminId);

    void deleteHotel(UUID adminId, UUID hotelId);

    List<HotelResponseDto> getAllHotels();
}
