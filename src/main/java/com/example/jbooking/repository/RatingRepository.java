package com.example.jbooking.repository;

import com.example.jbooking.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserIdAndHotelId(UUID id, UUID id1);

    List<Rating> findByHotelId(UUID hotelId);
}
