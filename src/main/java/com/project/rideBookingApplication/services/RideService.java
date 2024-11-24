package com.project.rideBookingApplication.services;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.RideRequest;
import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver Driver, PageRequest pageRequest);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
}
