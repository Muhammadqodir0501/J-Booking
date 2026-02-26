package com.example.jbooking.controller;

import com.example.jbooking.dto.request.CancellationRequestDto;
import com.example.jbooking.dto.request.CancellationUpdateDto;
import com.example.jbooking.dto.response.CancellationResponseDto;
import com.example.jbooking.entity.CancellationPolicy;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.CancellationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cancellations")
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService cancellationService;

    @PostMapping("/{userId}/{bookingId}")
    public ResponseEntity<ApiResponse<CancellationResponseDto>> cancelBooking(@PathVariable UUID bookingId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>(cancellationService.cancelBooking(currentUserId, bookingId)));
    }

    @PostMapping("/policies")
    public ResponseEntity<ApiResponse<CancellationPolicy>> createPolicy(
            @RequestBody @Valid CancellationRequestDto request) {
        return ResponseEntity.ok(new ApiResponse<>(cancellationService.createPolicy(request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CancellationPolicy>> updatePolicy(@RequestBody @Valid CancellationUpdateDto  request) {
        return ResponseEntity.ok(new ApiResponse<>(cancellationService.updatePolicy(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CancellationPolicy>>> getPolicies(){
        return ResponseEntity.ok(new ApiResponse<>(cancellationService.getAllPolicies()));
    }
}