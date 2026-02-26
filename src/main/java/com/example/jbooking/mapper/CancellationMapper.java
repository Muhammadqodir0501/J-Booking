package com.example.jbooking.mapper;

import com.example.jbooking.dto.response.CancellationResponseDto;
import com.example.jbooking.entity.CancelledBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CancellationMapper {


    @Mapping(target = "bookingId", source = "cancelledBooking.booking.id")
    @Mapping(target = "status", source = "cancelledBooking.booking.status")
    @Mapping(target = "message", source = "message")
    CancellationResponseDto toDto(CancelledBooking cancelledBooking, String message);
}
