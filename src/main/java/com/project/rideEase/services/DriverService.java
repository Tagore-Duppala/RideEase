package com.project.rideEase.services;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.RideRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    RideDto startRide(Long rideId, String otp);

    RideDto endRide(Long rideId);

    RideDto cancelRide(Long rideId);

    RiderDto rateRider(Long rideId, Double rating);

    DriverDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    RideDto acceptRide(Long rideRequestId);

    void updateDriverAvailability(Driver driver, Boolean status);

    Driver createNewDriver(Driver newDriver);

    String emailSubjectForAcceptRide();

    String emailBodyForAcceptRide(RideRequest rideRequest, Ride ride);

}
