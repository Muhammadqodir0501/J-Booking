package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.CancellationRequestDto;
import com.example.jbooking.dto.request.CancellationUpdateDto;
import com.example.jbooking.dto.response.CancellationResponseDto;
import com.example.jbooking.entity.CancellationPolicy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CancellationService {

    CancellationPolicy createPolicy(CancellationRequestDto request);

    CancellationResponseDto cancelBooking(UUID userId, UUID bookingId);

    CancellationPolicy updatePolicy(CancellationUpdateDto request);

    List<CancellationPolicy> getAllPolicies();
}
