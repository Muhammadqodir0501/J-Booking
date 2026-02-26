package com.example.jbooking.mapper;

import com.example.jbooking.dto.response.RoomTypeResponseDto;
import com.example.jbooking.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    @Mapping(target = "hotelName", source = "hotel.name")
    RoomTypeResponseDto toDto(RoomType entity);
}
