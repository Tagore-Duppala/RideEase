package com.project.rideBookingApplication.services;

import com.project.rideBookingApplication.dto.DriverDto;
import com.project.rideBookingApplication.dto.RideDto;
import com.project.rideBookingApplication.dto.RideRequestDto;
import com.project.rideBookingApplication.dto.RiderDto;
import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    RideRequestDto cancelRideRequest(Long rideRequestId);

    DriverDto rateDriver(Long rideId, Double rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}
