package com.project.rideBookingApplication.stratagies.Impl;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.RideRequest;
import com.project.rideBookingApplication.repositories.DriverRepository;
import com.project.rideBookingApplication.stratagies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearestDrivers(rideRequest.getPickUpLocation());
    }
}
