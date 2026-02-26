package com.example.jbooking.entity;

import com.example.jbooking.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rating_counters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCounter extends BaseEntity {
    float averageRating;
    int totalReviews;
    UUID hotelId;
}
