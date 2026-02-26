package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.CancellationRequestDto;
import com.example.jbooking.dto.request.CancellationUpdateDto;
import com.example.jbooking.dto.response.CancellationResponseDto;
import com.example.jbooking.entity.Booking;
import com.example.jbooking.entity.CancellationPolicy;
import com.example.jbooking.entity.CancelledBooking;
import com.example.jbooking.enums.BookingStatus;
import com.example.jbooking.exception.ConflictException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.CancellationMapper;
import com.example.jbooking.repository.*;
import com.example.jbooking.service.abstaction.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancellationServiceImpl implements CancellationService {

    private final BookingRepository bookingRepository;
    private final CancellationRepository cancellationRepository;
    private final CancelledBookingRepository cancelledBookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final CancellationMapper cancellationMapper;


    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CancellationPolicy createPolicy(CancellationRequestDto request) {

        roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        if (cancellationRepository.findByRoomTypeId(request.getRoomTypeId()).isPresent()) {
            throw new ConflictException("Политика для этого типа комнаты уже существует!");
        }

        CancellationPolicy policy = CancellationPolicy.builder()
                .roomTypeId(request.getRoomTypeId())
                .hoursBeforeCheckIn(request.getHoursBeforeCheckIn())
                .penaltyPercentage(request.getPenaltyPercentage())
                .build();

        return cancellationRepository.save(policy);
    }

    @Transactional
    @Override
    public CancellationResponseDto cancelBooking(UUID userId, UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено!"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new ConflictException("Вы не можете отменить чужое бронирование!");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictException("Это бронирование уже отменено!");
        }

        CancellationPolicy policy = cancellationRepository.findByRoomTypeId(booking.getRoom().getRoomType().getId())
                .orElseThrow(() -> new NotFoundException("Политика отмены для этого типа комнаты не найдена!"));

        LocalDateTime checkInDateTime = booking.getCheckInDate().atTime(LocalTime.of(14, 0));
        long hoursUntilCheckIn = ChronoUnit.HOURS.between(LocalDateTime.now(), checkInDateTime);

        BigDecimal fine = BigDecimal.ZERO;

        if (hoursUntilCheckIn < policy.getHoursBeforeCheckIn()) {
            BigDecimal penaltyPercent = BigDecimal.valueOf(policy.getPenaltyPercentage());

            fine = booking.getTotalPrice()
                    .multiply(penaltyPercent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        CancelledBooking cancelledBooking = CancelledBooking.builder()
                .booking(booking)
                .fine(fine)
                .description("Бронь отменена. Часов до заезда: " + hoursUntilCheckIn)
                .build();

        cancelledBooking = cancelledBookingRepository.save(cancelledBooking);
        return cancellationMapper.toDto(cancelledBooking, "Бронирование успешно отменено");
    }


    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CancellationPolicy updatePolicy(CancellationUpdateDto request){

        CancellationPolicy policy = cancellationRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new NotFoundException("Не верный ID"));

        policy.setHoursBeforeCheckIn(request.getHoursBeforeCheckIn());
        policy.setPenaltyPercentage(request.getPenaltyPercentage());
        return cancellationRepository.save(policy);
    }

    @Override
    public List<CancellationPolicy> getAllPolicies(){
        return cancellationRepository.findAll();
    }

}
