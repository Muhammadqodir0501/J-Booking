package com.example.jbooking.controller;

import com.example.jbooking.dto.request.RoomRequestDto;
import com.example.jbooking.dto.request.RoomSearchRequestDto;
import com.example.jbooking.dto.request.RoomUpdateRequestDto;
import com.example.jbooking.dto.response.RoomResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.service.abstaction.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRoom(@RequestBody @Valid RoomRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(roomService.createRoom(request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(@RequestBody @Valid RoomUpdateRequestDto request) {
        return ResponseEntity.ok(new ApiResponse<>(roomService.updateRoom(request)));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAvailableRooms(
            @Valid @ModelAttribute RoomSearchRequestDto searchRequest) {

        if (searchRequest.getCheckInDate().isAfter(searchRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Дата заезда не может быть позже даты выезда");
        }

        return ResponseEntity.ok(new ApiResponse<>(roomService.findAvailableRooms(
                searchRequest.getRoomTypeId(),
                searchRequest.getCheckInDate(),
                searchRequest.getCheckOutDate()
        )));
    }

    @GetMapping("/{roomTypeId}")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByRoomType(@PathVariable UUID roomTypeId) {
        return ResponseEntity.ok(new ApiResponse<>(roomService.findRoomsByRoomType(roomTypeId)));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAllRooms(@PathVariable UUID hotelId) {
        return ResponseEntity.ok(new ApiResponse<>(roomService.findAllRoomsByHotelId(hotelId)));
    }


    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> changeActivation(@PathVariable UUID roomId) {
        return ResponseEntity.ok(new ApiResponse<>(roomService.changeActivation(roomId)));
    }

}