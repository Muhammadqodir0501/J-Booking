package com.example.jbooking.controller;


import com.example.jbooking.dto.request.RoomTypeRequestDto;
import com.example.jbooking.dto.request.RoomTypeUpdateDto;
import com.example.jbooking.dto.response.RoomTypeResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @PostMapping()
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> createRoomType(
            @RequestBody @Valid RoomTypeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(roomTypeService.createRoomType(request)) );
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomTypeResponseDto>>> getRoomTypes(@PathVariable UUID hotelId) {
        return ResponseEntity.ok(new ApiResponse<>(roomTypeService.getRoomTypes(hotelId)));
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> updateRoomType(
            @RequestBody @Valid RoomTypeUpdateDto request) {
        UUID currentAdminId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>(roomTypeService.updateRoomType(currentAdminId, request)));
    }

    @DeleteMapping("/{adminId}/{roomTypeId}")
    public ResponseEntity<ApiResponse<Void>> deleteRoomType(@PathVariable UUID roomTypeId) {
        UUID currentAdminId = SecurityUtils.getCurrentUserId();
        roomTypeService.deleteRoomType(currentAdminId, roomTypeId);
        return ResponseEntity.noContent().build();
    }
}
