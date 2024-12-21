package com.project.rideEase.services;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RideRequestDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.User;
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

    String emailBodyForRideRequest(RideRequest rideRequest);

    String emailSubjectForRideRequest(RideRequest rideRequest);
}
