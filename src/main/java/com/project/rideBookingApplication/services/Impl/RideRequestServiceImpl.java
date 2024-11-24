package com.project.rideBookingApplication.services.Impl;

import com.project.rideBookingApplication.entities.RideRequest;
import com.project.rideBookingApplication.exceptions.ResourceNotFoundException;
import com.project.rideBookingApplication.repositories.RideRequestRepository;
import com.project.rideBookingApplication.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public void updateRideRequest(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("RideRequestId not found with id: "+ rideRequest.getId())
                );
        rideRequestRepository.save(rideRequest);


    }

    public RideRequest findRideRequestById(Long rideRequestId){
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(()-> new ResourceNotFoundException("RideRequest not found with id: "+rideRequestId));
    }
}
