package com.project.rideEase.services;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.Ride;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Double rating);

    RiderDto rateRider(Ride ride, Double rating);

    void createNewRating(Ride ride);

}
