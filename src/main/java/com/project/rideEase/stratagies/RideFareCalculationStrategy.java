package com.project.rideEase.stratagies;

import com.project.rideEase.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;
    double SURGE_PRICE = 2;
    double calculateFare(RideRequest rideRequest);
}
