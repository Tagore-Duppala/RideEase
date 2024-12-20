package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.services.DistanceService;
import com.project.rideEase.stratagies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),rideRequest.getDropLocation());

        return distance*RIDE_FARE_MULTIPLIER;
    }
}