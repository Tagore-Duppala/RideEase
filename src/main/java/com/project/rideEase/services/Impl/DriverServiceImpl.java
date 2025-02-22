package com.project.rideEase.services.Impl;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.RideRequest;
import com.project.rideEase.entities.User;
import com.project.rideEase.entities.enums.RideRequestStatus;
import com.project.rideEase.entities.enums.RideStatus;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.DriverRepository;
import com.project.rideEase.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final RideRequestService rideRequestService;
    private final PaymentService paymentService;
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public RideDto startRide(Long rideId, String otp) {

        try {
            Ride findRide = rideService.getRideById(rideId);

            if (!otp.equals(findRide.getOtp())) throw new RuntimeException("OTP is invalid: " + otp);
            if (!findRide.getRideStatus().equals(RideStatus.CONFIRMED))
                throw new RuntimeException("Ride status is not confirmed so cannot start the ride: " + findRide.getRideStatus());

            findRide.setStartedAt(LocalDateTime.now());
            paymentService.createPayment(findRide);
            rideService.updateRideStatus(findRide, RideStatus.ONGOING);
            ratingService.createNewRating(findRide);

            log.info("Ride started with ride id: " + rideId);
            return modelMapper.map(findRide, RideDto.class);
        }
        catch (Exception ex){
            log.error("Exception occurred for ride id {}, Error Msg: {}", rideId, ex.getMessage());
            throw new RuntimeException("Exception occurred while starting ride, Error Msg: "+ex.getMessage());
        }
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {

        try {
            Ride findRide = rideService.getRideById(rideId);

            if (!findRide.getDriver().equals(getCurrentDriver()))
                throw new RuntimeException("Driver is not authorized for cancelling the ride");
            if (!findRide.getRideStatus().equals(RideStatus.ONGOING))
                throw new RuntimeException("Ride status is not ONGOING so cannot end the ride: " + findRide.getRideStatus());

            Ride savedRide = rideService.updateRideStatus(findRide, RideStatus.ENDED);
            savedRide.setEndedAt(LocalDateTime.now());
            updateDriverAvailability(findRide.getDriver(), true);

            Duration duration = Duration.between(savedRide.getStartedAt(), savedRide.getEndedAt());
            savedRide.setDuration(duration.toMinutes());


            paymentService.processPayment(findRide);
            log.info("Ride completed");

            return modelMapper.map(savedRide, RideDto.class);
        }
        catch (Exception ex) {
            log.error("Exception occurred while ending the ride, Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred while ending ride: "+ex.getMessage());
        }
    }

    @Override
    @Transactional
    public RideDto cancelRide(Long rideId) {

        try {
            Ride ride = rideService.getRideById(rideId);
            Driver driver = getCurrentDriver();

            if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
                throw new RuntimeException("Cannot cancel the ride as ride has/is : " + ride.getRideStatus());
            if (!ride.getDriver().equals(driver)) throw new RuntimeException(("Driver cannot cancel the ride"));

            rideService.updateRideStatus(ride, RideStatus.CANCELLED);
            updateDriverAvailability(driver, true);
            log.info("Ride cancelled with id: " + rideId);

            return modelMapper.map(ride, RideDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in cancel ride , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in cancel ride: "+ex.getMessage());
        }
    }

    @Override
    public RiderDto rateRider(Long rideId, Double rating) {

        try {
            Ride ride = rideService.getRideById(rideId);
            if (!ride.getRideStatus().equals(RideStatus.ENDED)) throw new RuntimeException("Ride is not yet ended!");
            if (!getCurrentDriver().equals(ride.getDriver()))
                throw new RuntimeException("Current Driver is not assigned to this ride");

            return ratingService.rateRider(ride, rating);
        } catch (Exception ex) {
            log.error("Exception occurred in rateRider method , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in rateRider method: "+ex.getMessage());
        }

    }

    @Override
    public DriverDto getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver,DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {

        Driver driver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(driver, pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class));

    }

    @Override
    public Driver getCurrentDriver() {

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return driverRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Driver is not assosiated with userId :" + user.getId()));
        } catch (Exception ex) {
            log.error("Exception occurred in fetching current driver , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in fetching current driver: "+ex.getMessage());
        }
    }

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {

        try {
            RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
            if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.SEARCHING))
                throw new RuntimeException("RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatus());

            Driver driver = getCurrentDriver();
            if (!driver.getAvailable()) throw new RuntimeException("Driver cannot accept ride due to unavailability");

            Ride newRide = rideService.createNewRide(rideRequest, driver);
            updateDriverAvailability(newRide.getDriver(), false);
            log.info("Ride created! Sending email alert for the rider");

            emailSenderService.sendEmail(rideRequest.getRider().getUser().getEmail(), emailSubjectForAcceptRide(), emailBodyForAcceptRide(rideRequest, newRide));
            return modelMapper.map(newRide, RideDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in accept ride , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in accept ride: "+ex.getMessage());
        }

    }

    @Override
    public void updateDriverAvailability(Driver driver, Boolean status) {

        driver.setAvailable(status);
        driverRepository.save(driver);

    }

    @Override
    public Driver createNewDriver(Driver newDriver) {
        return driverRepository.save(newDriver);
    }

    @Override
    public String emailSubjectForAcceptRide() {
        return "Ride Accepted!";
    }

    @Override
    public String emailBodyForAcceptRide(RideRequest rideRequest, Ride ride) {

        String driverName =ride.getDriver().getUser().getName();
        String vehicleNo = ride.getDriver().getVehicleId();
        Double fare = rideRequest.getFare();
        String otp = ride.getOtp();
        String riderName = rideRequest.getRider().getUser().getName();

        return "<html>" +
                "<body>" +
                "<p>Hello,"+ riderName+"</p>" +
                "<p>Your ride is accepted, Driver is on the way</p>"+
                "<p>Driver Name: "+driverName+"</p>"+
                "<p>Vehicle No: "+ vehicleNo+"</p>"+
                "<p>Fare: "+ fare+"</p>"+
                "<p>OTP: "+ otp+"</p>"+
                "<p>Best regards,<br>Team RideEase</p>" + //TO DO, Add customer support link & SOS in the body
                "</body>" +
                "</html>";
    }
}
