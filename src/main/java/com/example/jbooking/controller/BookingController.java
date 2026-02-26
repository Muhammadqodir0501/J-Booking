package com.example.jbooking.controller;

import com.example.jbooking.dto.request.BookingRequestDto;
import com.example.jbooking.dto.response.BookingResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDto>> createBooking(
            @RequestBody @Valid BookingRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(bookingService.createBooking(request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getUserBookings() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>(bookingService.findUserBookings(currentUserId)));
    }

    @GetMapping("/{userId}/{hotelId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getUserBookingByHotel(@PathVariable UUID hotelId){
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>
                (bookingService.findUserBookingsByCurrentHotel(currentUserId, hotelId)));
    }


}