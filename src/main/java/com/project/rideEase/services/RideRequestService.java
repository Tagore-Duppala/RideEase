package com.project.rideEase.services;


import com.project.rideEase.entities.RideRequest;

public interface RideRequestService {

    void updateRideRequest(RideRequest rideRequest);

    RideRequest findRideRequestById(Long rideRequestId);
}
