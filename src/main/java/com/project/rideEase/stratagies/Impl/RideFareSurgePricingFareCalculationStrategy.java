package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.services.DistanceService;
import com.project.rideEase.stratagies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double[] distance= distanceService.calculateDistance(rideRequest.getPickUpLocation(),rideRequest.getDropLocation());
        return distance[0]*RIDE_FARE_MULTIPLIER*SURGE_PRICE;
    }
}
