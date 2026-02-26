package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.RoomTypeRequestDto;
import com.example.jbooking.dto.request.RoomTypeUpdateDto;
import com.example.jbooking.dto.response.RoomTypeResponseDto;
import com.example.jbooking.entity.Hotel;
import com.example.jbooking.entity.Room;
import com.example.jbooking.entity.RoomType;
import com.example.jbooking.exception.ConflictException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.RoomTypeMapper;
import com.example.jbooking.repository.HotelRepository;
import com.example.jbooking.repository.RoomRepository;
import com.example.jbooking.repository.RoomTypeRepository;
import com.example.jbooking.service.abstaction.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomRepository roomRepository;

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoomTypeResponseDto createRoomType(RoomTypeRequestDto request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException("Отель не найден"));

        RoomType newRoomType = RoomType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .capacityChildren(request.getCapacityChildren())
                .capacityAdults(request.getCapacityAdults())
                .hotel(hotel)
                .build();

        return roomTypeMapper.toDto(roomTypeRepository.save(newRoomType));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoomTypeResponseDto updateRoomType(UUID adminId, RoomTypeUpdateDto request) {

        RoomType existRoomType = roomTypeRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        if(!existRoomType.getHotel().getAdmin().getId().equals(adminId)) {
            throw new ConflictException("Неверный ID админа!");
        }

        existRoomType.setName(request.getName());
        existRoomType.setDescription(request.getDescription());
        existRoomType.setCapacityChildren(request.getCapacityChildren());
        existRoomType.setCapacityAdults(request.getCapacityAdults());
        return roomTypeMapper.toDto(roomTypeRepository.save(existRoomType));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoomType(UUID adminId, UUID roomTypeId) {

        RoomType existRoomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        if(!existRoomType.getHotel().getAdmin().getId().equals(adminId)) {
            throw new ConflictException("Неверный ID админа!");
        }

        if (roomRepository.existsByRoomTypeId(roomTypeId)) {
            throw new ConflictException("Вы не можете удалить тип комнаты. К нему привязаны физические комнаты! ");
        }

        roomTypeRepository.delete(existRoomType);
    }

    @Override
    public List<RoomTypeResponseDto> getRoomTypes(UUID hotelId) {
        hotelRepository.findById(hotelId).orElseThrow(()-> new NotFoundException("Отель не найден!"));
        return roomTypeRepository.findByHotelId(hotelId).stream()
                .map(roomTypeMapper::toDto)
                .toList();
    }
}