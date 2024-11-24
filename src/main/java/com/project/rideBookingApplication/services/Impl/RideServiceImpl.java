package com.project.rideBookingApplication.services.Impl;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.RideRequest;
import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.enums.RideRequestStatus;
import com.project.rideBookingApplication.entities.enums.RideStatus;
import com.project.rideBookingApplication.exceptions.ResourceNotFoundException;
import com.project.rideBookingApplication.repositories.RideRepository;
import com.project.rideBookingApplication.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final ModelMapper modelMapper;
    private final RideRepository rideRepository;

    @Override
    public Ride getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() ->
                new ResourceNotFoundException("Couldn't find Ride with Ride id: "+rideId));
        return ride;
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setOtp(generateOTP());
        ride.setDriver(driver);
        rideRepository.save(ride);


        return ride;

    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    public String generateOTP(){
        Random random = new Random();
        int otp = random.nextInt(10000);
        return String.format("%04d",otp);
    }
}

