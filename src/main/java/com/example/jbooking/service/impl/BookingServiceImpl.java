package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.BookingRequestDto;
import com.example.jbooking.dto.response.BookingResponseDto;
import com.example.jbooking.entity.Booking;
import com.example.jbooking.entity.Room;
import com.example.jbooking.entity.User;
import com.example.jbooking.enums.BookingStatus;
import com.example.jbooking.exception.ForbiddenException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.BookingMapper;
import com.example.jbooking.repository.BookingRepository;
import com.example.jbooking.repository.HotelRepository;
import com.example.jbooking.repository.RoomRepository;
import com.example.jbooking.repository.UserRepository;
import com.example.jbooking.service.abstaction.BookingService;
import com.example.jbooking.service.abstaction.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final HotelRepository hotelRepository;
    private final PaymentService paymentService;

    @Transactional
    @Override
    public BookingResponseDto createBooking(BookingRequestDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Комната не найдена"));

        if(!room.isActive()){
            throw new ForbiddenException("Комната занята");
        }

        boolean isRoomTaken = bookingRepository.existsOverlappingBooking(
                room.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (isRoomTaken) {
            throw new ForbiddenException("Извините, комнату уже забронировали на выбранные даты!");
        }

        long days = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (days < 1) throw new ForbiddenException("Минимум 1 ночь");

        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(days));

        Booking newBooking = Booking.builder()
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .user(user)
                .room(room)
                .hotelId(room.getRoomType().getHotel().getId())
                .totalPrice(totalPrice)
                .status(BookingStatus.CREATED)
                .build();

        newBooking = bookingRepository.save(newBooking);

        paymentService.initiatePayment(
                newBooking.getId(),
                totalPrice,
                request.getSenderToken()
        );

        return bookingMapper.toDto(newBooking);
    }

    @Override
    public List<BookingResponseDto> findUserBookings(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("Юзер не нашелся"));

        return bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> findUserBookingsByCurrentHotel(UUID userId, UUID hotelId) {
        userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("Юзер не нашелся"));

        hotelRepository.findById(hotelId)
                .orElseThrow(()-> new NotFoundException("Отель не нашелся"));

        return bookingRepository.findAllByUserIdAndHotelId(userId,hotelId)
                .stream().map(bookingMapper::toDto).toList();
    }



}