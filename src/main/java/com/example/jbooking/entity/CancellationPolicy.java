package com.example.jbooking.entity;

import com.example.jbooking.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "cancellation_policies")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CancellationPolicy extends BaseEntity {

    private UUID roomTypeId;
    private int hoursBeforeCheckIn;
    private int penaltyPercentage;
}
