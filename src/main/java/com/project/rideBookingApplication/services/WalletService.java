package com.project.rideBookingApplication.services;

import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.User;
import com.project.rideBookingApplication.entities.Wallet;
import com.project.rideBookingApplication.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
