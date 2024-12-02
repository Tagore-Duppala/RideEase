package com.project.rideBookingApplication.repositories;

import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Long> {
    Optional<Rider> findByUser(User user);
}
