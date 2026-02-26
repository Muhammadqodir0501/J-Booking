package com.example.jbooking.repository;

import com.example.jbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("SELECT r FROM Room r " +
            "WHERE r.roomType.id = :roomTypeId " +
            "AND r.active = true " +
            "AND r.id NOT IN (" +
            "  SELECT b.room.id FROM Booking b " +
            "  WHERE b.status != 'CANCELLED' " +
            "  AND (" +
            "    (b.checkInDate < :checkOut) AND (b.checkOutDate > :checkIn)" +
            "  )" +
            ")")
    List<Room> findAvailableRooms(
            @Param("roomTypeId") UUID roomTypeId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    List<Room> findByHotelId(UUID hotelId);

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomTypeId(UUID roomTypeId);

    List<Room> findAllByRoomTypeId(UUID roomTypeId);

    List<Room> findAllByHotelId(UUID hotelId);

}