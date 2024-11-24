package com.project.rideBookingApplication.services;

import com.project.rideBookingApplication.dto.DriverDto;
import com.project.rideBookingApplication.dto.RiderDto;
import com.project.rideBookingApplication.entities.Ride;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Double rating);

    RiderDto rateRider(Ride ride, Double rating);

    void createNewRating(Ride ride);

}
