package com.project.rideEase.services.Impl;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.enums.RideRequestStatus;
import com.project.rideEase.entities.enums.RideStatus;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.RideRepository;
import com.project.rideEase.services.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private final ModelMapper modelMapper;
    private final RideRepository rideRepository;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(() ->
                new ResourceNotFoundException("Couldn't find Ride with Ride id: "+rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {

        try {
            rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

            Ride ride = modelMapper.map(rideRequest, Ride.class);
            ride.setRideStatus(RideStatus.CONFIRMED);
            ride.setOtp(generateOTP());
            ride.setDriver(driver);
            rideRepository.save(ride);
            log.info("New ride created!");

            return ride;
        } catch (Exception ex) {
            log.error("Exception occurred in createNewRide , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in createNewRide: "+ex.getMessage());
        }
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        try {
            return rideRepository.findByRider(rider, pageRequest);
        } catch (Exception ex) {
            log.error("Exception occurred in getAllRidesOfRider , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception getAllRidesOfRider: "+ex.getMessage());
        }
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {

        try {
            return rideRepository.findByDriver(driver, pageRequest);
        } catch (Exception ex) {
            log.error("Exception occurred in getAllRidesOfDriver , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getAllRidesOfDriver: "+ex.getMessage());
        }
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        log.info("Ride status updated!");
        return rideRepository.save(ride);
    }

    public String generateOTP(){
        Random random = new Random();
        int otp = random.nextInt(10000);
        log.info("OTP generated");
        return String.format("%04d",otp);
    }
}

