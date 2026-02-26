package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.RoomRequestDto;
import com.example.jbooking.dto.request.RoomUpdateRequestDto;
import com.example.jbooking.dto.response.RoomResponseDto;
import com.example.jbooking.entity.Room;
import com.example.jbooking.entity.RoomType;
import com.example.jbooking.exception.ConflictException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.RoomMapper;
import com.example.jbooking.repository.RoomRepository;
import com.example.jbooking.repository.RoomTypeRepository;
import com.example.jbooking.service.abstaction.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponseDto createRoom(RoomRequestDto request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден"));

        Room room = Room.builder()
                .roomType(roomType)
                .roomNumber(request.getRoomNumber())
                .price(request.getPrice())
                .hotelId(roomType.getHotel().getId())
                .build();

        return roomMapper.toDto(roomRepository.save(room));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponseDto updateRoom(RoomUpdateRequestDto request) {
        Room updatingRoom = roomRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Комната не найдена!"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        roomRepository.findByRoomNumber(request.getRoomNumber())
                .filter(existing -> !existing.getId().equals(request.getId()))
                .ifPresent(existing -> {
                    throw new ConflictException("Комната с таким номером уже существует!");
                });
        updatingRoom.setRoomNumber(request.getRoomNumber());
        updatingRoom.setPrice(request.getPrice());
        updatingRoom.setRoomType(roomType);


        return  roomMapper.toDto(roomRepository.save(updatingRoom));
    }

    @Override
    public List<RoomResponseDto> findAvailableRooms(UUID roomTypeId, LocalDate checkIn, LocalDate checkOut) {

        roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        List<Room> availableRooms = roomRepository.findAvailableRooms(roomTypeId, checkIn, checkOut);

        return availableRooms.stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    public List<RoomResponseDto> findRoomsByRoomType(UUID roomTypeId) {
        roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new NotFoundException("Тип комнаты не найден!"));

        return roomRepository.findAllByRoomTypeId(roomTypeId).stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    public List<RoomResponseDto>  findAllRoomsByHotelId(UUID hotelId){
        return roomRepository.findAllByHotelId(hotelId).stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponseDto changeActivation(UUID roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Комната не найдена!"));

        room.setActive(!room.isActive());
        return roomMapper.toDto(roomRepository.save(room));
    }
}