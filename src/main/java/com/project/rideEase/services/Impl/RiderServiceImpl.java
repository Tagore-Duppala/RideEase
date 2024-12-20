package com.project.rideEase.services.Impl;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RideRequestDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.User;
import com.project.rideEase.entities.enums.RideRequestStatus;
import com.project.rideEase.entities.enums.RideStatus;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.RideRepository;
import com.project.rideEase.repositories.RideRequestRepository;
import com.project.rideEase.repositories.RiderRepository;
import com.project.rideEase.services.DriverService;
import com.project.rideEase.services.RatingService;
import com.project.rideEase.services.RideService;
import com.project.rideEase.services.RiderService;
import com.project.rideEase.stratagies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j //to get the logs
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideStrategyManager rideStrategyManager;
    private final RideService rideService;
    private final DriverService driverService;
    private final RideRepository rideRepository;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider  = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto,RideRequest.class); //for converting PointDto in riderrqstDto to Point in RideRequestEnitiy we define a typematch in mapperconfig
//        log.info("Ride request ----------> ",rideRequest);
        rideRequest.setRideRequestStatus(RideRequestStatus.SEARCHING);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);

        rideRequest.setRider(rider);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(savedRideRequest);

        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }


    @Override
    public RideDto cancelRide(Long rideId) {

        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())) throw new RuntimeException("Rider doesn't own this ride: "+rideId);
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) throw new RuntimeException(("Ride cannot be cancelled as it has/is: "+ride.getRideStatus() ));

        rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(),true);

        return modelMapper.map(ride, RideDto.class);

    }

    @Override
    public RideRequestDto cancelRideRequest(Long rideRequestId) {
        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(()->new ResourceNotFoundException("RideRequest not found with Id: "+rideRequestId));
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.SEARCHING)) throw new RuntimeException("Ride is already booked! Please cancel the ride instead");

        rideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);
        rideRequestRepository.save(rideRequest);
        return modelMapper.map(rideRequest,RideRequestDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Double rating) {
        Ride ride = rideService.getRideById(rideId);

        if(!ride.getRideStatus().equals(RideStatus.ENDED)) throw new RuntimeException("Ride is not yet ended!");
        if(!getCurrentRider().equals(ride.getDriver())) throw new RuntimeException("Ride doesn't belong to current rider");

        return ratingService.rateDriver(ride,rating);
    }

    @Override
    public RiderDto getMyProfile() {
        return modelMapper.map(getCurrentRider(),RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider rider = getCurrentRider();
        return rideService.getAllRidesOfRider(rider,pageRequest)
                .map(ride -> modelMapper.map(ride,RideDto.class));
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider.builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Rider not found with userId :"+user.getId()));
    }
}
