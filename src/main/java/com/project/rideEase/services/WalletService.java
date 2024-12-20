package com.project.rideEase.services;

import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.User;
import com.project.rideEase.entities.Wallet;
import com.project.rideEase.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
