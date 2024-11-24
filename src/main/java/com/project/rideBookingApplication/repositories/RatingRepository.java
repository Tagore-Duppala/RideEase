package com.project.rideBookingApplication.repositories;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Rating;
import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}
