package com.example.jbooking.repository;

import com.example.jbooking.entity.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CancellationRepository extends JpaRepository<CancellationPolicy, UUID> {
    Optional<CancellationPolicy> findByRoomTypeId(UUID roomTypeId);
}
