package com.project.rideEase.repositories;

import com.project.rideEase.entities.User;
import com.project.rideEase.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByUser(User user);

}
