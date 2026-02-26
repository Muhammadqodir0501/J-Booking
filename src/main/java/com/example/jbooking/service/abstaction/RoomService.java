package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.RoomRequestDto;
import com.example.jbooking.dto.request.RoomUpdateRequestDto;
import com.example.jbooking.dto.response.RoomResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomService {
    @Transactional
    RoomResponseDto createRoom(RoomRequestDto request);

    RoomResponseDto updateRoom(RoomUpdateRequestDto request);

    List<RoomResponseDto> findAvailableRooms(UUID roomTypeId, LocalDate checkIn, LocalDate checkOut);

    List<RoomResponseDto> findRoomsByRoomType(UUID roomTypeId);

    List<RoomResponseDto>  findAllRoomsByHotelId(UUID hotelId);

    RoomResponseDto changeActivation(UUID roomId);
}
