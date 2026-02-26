package com.example.jbooking.mapper;

import com.example.jbooking.dto.request.RoomRequestDto;
import com.example.jbooking.dto.response.RoomResponseDto;
import com.example.jbooking.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "roomTypeName", source = "roomType.name")
    @Mapping(target = "capacityAdults", source = "roomType.capacityAdults")
    RoomResponseDto toDto(Room entity);
}