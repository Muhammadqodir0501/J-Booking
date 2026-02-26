package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.RatingRequestDto;
import com.example.jbooking.dto.response.HotelResponseDto;
import com.example.jbooking.entity.Hotel;
import com.example.jbooking.entity.Rating;
import com.example.jbooking.entity.RatingCounter;
import com.example.jbooking.entity.User;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.HotelMapper;
import com.example.jbooking.repository.HotelRepository;
import com.example.jbooking.repository.RatingCounterRepository;
import com.example.jbooking.repository.RatingRepository;
import com.example.jbooking.repository.UserRepository;
import com.example.jbooking.service.abstaction.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingCounterRepository ratingCounterRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final HotelMapper hotelMapper;

    @Transactional
    @Override
    public HotelResponseDto addRating(UUID userId , RatingRequestDto ratingDto) {

        Hotel hotel = hotelRepository.findById(ratingDto.getHotelId())
                .orElseThrow(() -> new NotFoundException("Отель не найден!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Юзер не найден!"));

        RatingCounter counter = ratingCounterRepository.findByHotelId(hotel.getId())
                .orElseGet(() -> RatingCounter.builder()
                        .hotelId(hotel.getId())
                        .averageRating(0f)
                        .totalReviews(0)
                        .build());

        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndHotelId(user.getId(), hotel.getId());

        if (existingRatingOpt.isPresent()) {

            Rating existingRating = existingRatingOpt.get();
            float oldRating = existingRating.getRating();
            float newRating = ratingDto.getRating();

            float newAvg = ((counter.getAverageRating() * counter.getTotalReviews()) - oldRating + newRating) / counter.getTotalReviews();

            counter.setAverageRating(newAvg);
            existingRating.setRating(newRating);
        } else {
            Rating newRating = Rating.builder()
                    .user(user)
                    .hotel(hotel)
                    .rating(ratingDto.getRating())
                    .build();
            ratingRepository.save(newRating);

            int currentTotal = counter.getTotalReviews();
            float newAvg = ((counter.getAverageRating() * currentTotal) + ratingDto.getRating()) / (currentTotal + 1);

            counter.setAverageRating(newAvg);
            counter.setTotalReviews(currentTotal + 1);
        }

        ratingCounterRepository.save(counter);
        hotel.setRating(counter.getAverageRating());

        return hotelMapper.toDto(hotelRepository.save(hotel));
    }
}
