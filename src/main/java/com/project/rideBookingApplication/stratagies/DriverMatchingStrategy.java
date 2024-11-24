package com.project.rideBookingApplication.stratagies;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
