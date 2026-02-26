package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.HotelRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import com.example.jbooking.entity.*;
import com.example.jbooking.enums.Role;
import com.example.jbooking.exception.ConflictException;
import com.example.jbooking.exception.ForbiddenException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.HotelMapper;
import com.example.jbooking.repository.*;
import com.example.jbooking.service.abstaction.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RatingRepository ratingRepository;
    private final RatingCounterRepository ratingCounterRepository;
    private final CancelledBookingRepository cancelledBookingRepository;

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponseDto createHotel(HotelRequestDto request, UUID adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Админ с такой ID не существует"));

        if(!admin.getRole().equals(Role.ADMIN)){
            throw new ConflictException("Пользователи не могут создать Отель");
        }

        if(hotelRepository.existsByName(request.getName())) {
            throw new ConflictException("Отель с таким именем уже существует");
        }

        Hotel newHotel = Hotel.builder()
                .admin(admin)
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .address(request.getAddress())
                .brand(request.getBrand())
                .description(request.getDescription())
                .amenities(request.getAmenities())
                .build();
        return hotelMapper.toDto(hotelRepository.save(newHotel));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteHotel(UUID adminId, UUID hotelId) {

        userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Админ не нашелся!"));

        Hotel existHotel =hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Отель не найден!"));

        if(!existHotel.getAdmin().getId().equals(adminId)) {
            throw new ForbiddenException("Отель не принадлежит этому админу! ");
        }

        List<Rating> ratings = ratingRepository.findByHotelId(hotelId);
        ratingRepository.deleteAll(ratings);

        List<RatingCounter> counters = ratingCounterRepository.findAllByHotelId(hotelId);
        ratingCounterRepository.deleteAll(counters);

        List<Booking> bookings = bookingRepository.findByHotelId(hotelId);
        bookingRepository.deleteAll(bookings);

        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        roomRepository.deleteAll(rooms);

        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);
        roomTypeRepository.deleteAll(roomTypes);

        hotelRepository.deleteById(hotelId);
    }


    @Override
    public List<HotelResponseDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toDto)
                .toList();
    }
}