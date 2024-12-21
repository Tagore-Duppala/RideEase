package com.project.rideEase.services.Impl;

import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.User;
import com.project.rideEase.entities.Wallet;
import com.project.rideEase.entities.WalletTransaction;
import com.project.rideEase.entities.enums.TransactionMethod;
import com.project.rideEase.entities.enums.TransactionType;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.WalletRepository;
import com.project.rideEase.services.WalletService;
import com.project.rideEase.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);

        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .transactionMethod(transactionMethod)
                .transactionType(TransactionType.CREDIT)
                .amount(amount)
                .wallet(wallet)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        wallet.getTransactions().add(walletTransaction);
        log.info("Money added to the Wallet");
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        if(wallet.getBalance()<amount) throw new RuntimeException("There is no enough balance in the wallet, Please add money to continue, Balance: "+wallet.getBalance());
        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .transactionMethod(transactionMethod)
                .transactionType(TransactionType.DEBIT)
                .amount(amount)
                .wallet(wallet)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        wallet.getTransactions().add(walletTransaction);
        log.info("Money deducted from the wallet");
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(()->new ResourceNotFoundException("Wallet not found with Id: "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user);
    }
}
