package com.example.jbooking.repository;

import com.example.jbooking.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {

    List<RoomType> findByHotelId(UUID hotelId);
}