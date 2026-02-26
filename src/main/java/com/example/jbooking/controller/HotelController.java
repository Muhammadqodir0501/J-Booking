package com.example.jbooking.controller;

import com.example.jbooking.dto.request.HotelRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping()
    public ResponseEntity<ApiResponse<HotelResponseDto>> createHotel
            (@RequestBody @Valid HotelRequestDto request) {
        UUID currentAdminId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(hotelService.createHotel(request, currentAdminId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelResponseDto>>> getAllHotels() {
        return ResponseEntity.ok(new ApiResponse<>(hotelService.getAllHotels()));
    }

    @DeleteMapping("/{adminId}/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponseDto>> deleteHotel(@PathVariable UUID hotelId) {
        UUID currentAdminId = SecurityUtils.getCurrentUserId();
        hotelService.deleteHotel(currentAdminId, hotelId);
        return ResponseEntity.noContent().build();
    }


}