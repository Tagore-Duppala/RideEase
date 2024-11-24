package com.project.rideBookingApplication.stratagies;

import com.project.rideBookingApplication.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;
    double SURGE_PRICE = 2;
    double calculateFare(RideRequest rideRequest);
}
