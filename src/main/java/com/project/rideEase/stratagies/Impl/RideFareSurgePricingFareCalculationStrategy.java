package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.services.DistanceService;
import com.project.rideEase.stratagies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {

        try {
            Double[] distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(), rideRequest.getDropLocation());
            return distance[0] * RIDE_FARE_MULTIPLIER * SURGE_PRICE;
        } catch (Exception ex) {
            log.error("Exception occurred in calculateFare , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in calculateFare: "+ex.getMessage());
        }
    }
}
