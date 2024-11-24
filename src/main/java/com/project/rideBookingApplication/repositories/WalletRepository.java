package com.project.rideBookingApplication.repositories;

import com.project.rideBookingApplication.entities.User;
import com.project.rideBookingApplication.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByUser(User user);

}
