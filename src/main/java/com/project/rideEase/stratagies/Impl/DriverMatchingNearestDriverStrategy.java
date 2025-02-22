package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.repositories.DriverRepository;
import com.project.rideEase.stratagies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        try {
            return driverRepository.findTenNearestDrivers(rideRequest.getPickUpLocation());
        } catch (Exception ex) {
            log.error("Exception occurred in findMatchingDriver , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in findMatchingDriver: "+ex.getMessage());
        }
    }
}
