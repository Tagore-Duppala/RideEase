package com.project.rideEase.stratagies;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
