package com.project.rideEase.repositories;

import com.project.rideEase.entities.Payment;
import com.project.rideEase.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByRide(Ride ride);
}
