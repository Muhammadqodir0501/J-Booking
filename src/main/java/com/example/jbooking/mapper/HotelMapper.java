package com.example.jbooking.mapper;

import com.example.jbooking.dto.response.HotelResponseDto;
import com.example.jbooking.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "adminId", source = "admin.id")
    HotelResponseDto toDto(Hotel entity);
}
