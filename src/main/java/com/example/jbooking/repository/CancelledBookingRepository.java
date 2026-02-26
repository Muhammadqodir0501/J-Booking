package com.example.jbooking.repository;

import com.example.jbooking.entity.CancelledBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CancelledBookingRepository extends JpaRepository<CancelledBooking, UUID> {
    Optional <CancelledBooking> findByBookingId(UUID id);

}

