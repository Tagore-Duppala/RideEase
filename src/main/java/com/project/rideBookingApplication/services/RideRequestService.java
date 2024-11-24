package com.project.rideBookingApplication.services;


import com.project.rideBookingApplication.entities.RideRequest;

public interface RideRequestService {

    void updateRideRequest(RideRequest rideRequest);

    RideRequest findRideRequestById(Long rideRequestId);
}
