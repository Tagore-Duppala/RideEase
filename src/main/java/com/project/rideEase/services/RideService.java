package com.project.rideEase.services;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver Driver, PageRequest pageRequest);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
}
