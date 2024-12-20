package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.repositories.DriverRepository;
import com.project.rideEase.stratagies.DriverMatchingStrategy;
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
