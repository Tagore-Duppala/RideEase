package com.project.rideBookingApplication.repositories;

import com.project.rideBookingApplication.entities.Payment;
import com.project.rideBookingApplication.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByRide(Ride ride);
}
