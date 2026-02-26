package com.example.jbooking.controller;

import com.example.jbooking.dto.request.RatingRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<ApiResponse<HotelResponseDto>> addRating(@RequestBody RatingRequestDto ratingDto) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>( ratingService.addRating(currentUserId ,ratingDto)));
    }
}
