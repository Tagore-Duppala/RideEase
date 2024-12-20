package com.project.rideEase.repositories;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Rating;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.Rider;
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
