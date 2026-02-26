package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.RoomTypeRequestDto;
import com.example.jbooking.dto.request.RoomTypeUpdateDto;
import com.example.jbooking.dto.response.RoomTypeResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {
    @Transactional
    RoomTypeResponseDto createRoomType(RoomTypeRequestDto request);

    RoomTypeResponseDto updateRoomType(UUID adminId, RoomTypeUpdateDto request);

    void deleteRoomType(UUID adminId, UUID roomTypeId);

    List<RoomTypeResponseDto> getRoomTypes(UUID hotelId);
}
