package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.RatingRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface RatingService {

    @Transactional
    HotelResponseDto addRating(UUID userId, RatingRequestDto ratingDto);
}
