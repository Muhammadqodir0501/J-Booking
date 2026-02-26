package com.example.jbooking.mapper;

import com.example.jbooking.dto.response.BookingResponseDto;
import com.example.jbooking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "roomTypeName", source = "room.roomType.name")
    BookingResponseDto toDto(Booking entity);
}