package com.project.rideEase.services.Impl;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RideRequestDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.*;
import com.project.rideEase.entities.enums.RideRequestStatus;
import com.project.rideEase.entities.enums.RideStatus;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.RideRepository;
import com.project.rideEase.repositories.RideRequestRepository;
import com.project.rideEase.repositories.RiderRepository;
import com.project.rideEase.services.*;
import com.project.rideEase.stratagies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider  = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto,RideRequest.class); //for converting PointDto in riderrqstDto to Point in RideRequestEnitiy we define a typematch in mapperconfig
        rideRequest.setRideRequestStatus(RideRequestStatus.SEARCHING);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);

        rideRequest.setRider(rider);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers= rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(savedRideRequest);
        List<String> driversEmail = drivers.stream()
                .map(driverslist-> driverslist.getUser().getEmail())
                .toList();

        log.info("List of driver emails: "+driversEmail);

        emailSenderService.sendEmail(driversEmail.toArray(new String[0]),emailSubjectForRideRequest(rideRequest),emailBodyForRideRequest(rideRequest));
        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }


    @Override
    @Transactional
    public RideDto cancelRide(Long rideId) {

        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())) throw new RuntimeException("Rider doesn't own this ride: "+rideId);
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) throw new RuntimeException(("Ride cannot be cancelled as it has/is: "+ride.getRideStatus() ));

        rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(),true);

        log.info("Ride successfully cancelled by rider with id: "+ rider.getId());
        return modelMapper.map(ride, RideDto.class);

    }

    @Override
    @Transactional
    public RideRequestDto cancelRideRequest(Long rideRequestId) {
        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(()->new ResourceNotFoundException("RideRequest not found with Id: "+rideRequestId));
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.SEARCHING)) throw new RuntimeException("Ride is already booked! Please cancel the ride instead");
        if(!rideRequest.getRider().equals(getCurrentRider())) throw new RuntimeException("You are not authorized to cancel this ride");

        rideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);
        rideRequestRepository.save(rideRequest);

        log.info("Ride request successfully cancelled by rider with id: "+ rideRequest.getRider().getId());
        return modelMapper.map(rideRequest,RideRequestDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Double rating) {
        Ride ride = rideService.getRideById(rideId);

        if(!ride.getRideStatus().equals(RideStatus.ENDED)) throw new RuntimeException("Ride is not yet ended!");
        if(!getCurrentRider().equals(ride.getDriver())) throw new RuntimeException("Ride doesn't belong to current rider");

        log.info("Thanks for you rating and feedback for driver with id: "+ ride.getDriver().getId());
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

    @Override
    public String emailBodyForRideRequest(RideRequest rideRequest) {

        Point pickUpLocation = rideRequest.getPickUpLocation();
        Point dropLocation = rideRequest.getDropLocation();
        Double fare = rideRequest.getFare();
        String acceptRideUrl = "http://localhost:8080/driver/acceptRide/"+rideRequest.getId(); //TO DO after deploying into AWS

        return "<html>" +
                "<body>" +
                "<p>Hello,</p>" +
                "<p>New Ride requested, Below are the details</p>"+
                "<p>Pickup Location: "+pickUpLocation+"</p>"+
                "<p>Drop Location: "+ dropLocation+"</p>"+
                "<p>Fare: "+ fare+"</p>"+
                "<p>Click the following link to accept the ride:</p>" +
                "<a href=\"" + acceptRideUrl + "\">Accept Ride</a>" +
                "<p>Best regards,<br>Team RideEase</p>" +
                "</body>" +
                "</html>"
                ;
    }

    @Override
    public String emailSubjectForRideRequest(RideRequest rideRequest) {
        return "New Ride Requested, Fare: "+rideRequest.getFare();
    }


}
