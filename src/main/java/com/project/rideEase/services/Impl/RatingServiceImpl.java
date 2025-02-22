package com.project.rideEase.services.Impl;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Rating;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.exceptions.RuntimeConflictException;
import com.project.rideEase.repositories.DriverRepository;
import com.project.rideEase.repositories.RatingRepository;
import com.project.rideEase.repositories.RiderRepository;
import com.project.rideEase.services.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService{

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final RiderRepository riderRepository;

    @Override
    public DriverDto rateDriver(Ride ride, Double rating) {

        try {
            Rating rating1 = ratingRepository.findByRide(ride)
                    .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with Id: " + ride.getId()));
            if (rating1.getDriverRating() != null)
                throw new RuntimeConflictException("Driver was already rated for this ride, Cannot rate again");

            rating1.setDriverRating(rating);
            ratingRepository.save(rating1); // setting rating for the particular ride

            Double newRatingAvg = ratingRepository.findByDriver(ride.getDriver())
                    .stream()
                    .mapToDouble(rating2 -> rating2.getDriverRating())
                    .average()
                    .orElse(0.0);
            ride.getDriver().setRating(newRatingAvg); //setting rating avg for the particular driver

            Driver savedDriver = driverRepository.save(ride.getDriver());
            return modelMapper.map(savedDriver, DriverDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in rateDriver , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in rateDriver: "+ex.getMessage());
        }
    }

    @Override
    public RiderDto rateRider(Ride ride, Double rating) {

        try {
            Rating rating1 = ratingRepository.findByRide(ride)
                    .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with Id: " + ride.getId()));
            if (rating1.getRiderRating() != null)
                throw new RuntimeConflictException("Rider was already rated for this ride, Cannot rate again");

            rating1.setRiderRating(rating);
            ratingRepository.save(rating1); // setting rating for the particular ride

            Double newRatingAvg = ratingRepository.findByRider(ride.getRider())
                    .stream()
                    .mapToDouble(rating2 -> rating2.getRiderRating())
                    .average()
                    .orElse(0.0);
            ride.getRider().setRating(newRatingAvg); //setting rating avg for the particular driver

            Rider savedRider = riderRepository.save(ride.getRider());
            return modelMapper.map(savedRider, RiderDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in rateRider , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in rateRider: "+ex.getMessage());
        }
    }

    @Override
    public void createNewRating(Ride ride) {

        try {
            Rating rating = Rating.builder()
                    .driver(ride.getDriver())
                    .rider(ride.getRider())
                    .build();
            ratingRepository.save(rating);
        } catch (Exception ex) {
            log.error("Exception occurred in createNewRating , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in createNewRating: "+ex.getMessage());
        }

    }
}
