package com.example.jbooking.repository;

import com.example.jbooking.entity.RatingCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingCounterRepository extends JpaRepository<RatingCounter, UUID> {
    Optional<RatingCounter> findByHotelId(UUID hotelId);

    List<RatingCounter> findAllByHotelId(UUID id);

}
