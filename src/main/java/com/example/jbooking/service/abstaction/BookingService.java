package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.BookingRequestDto;
import com.example.jbooking.dto.response.BookingResponseDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto request);

    List<BookingResponseDto> findUserBookings(UUID userId);

    List<BookingResponseDto> findUserBookingsByCurrentHotel(UUID userId, UUID hotelId);
}
