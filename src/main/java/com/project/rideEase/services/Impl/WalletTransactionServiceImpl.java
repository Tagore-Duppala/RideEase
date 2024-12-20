package com.project.rideEase.services.Impl;

import com.project.rideEase.entities.WalletTransaction;
import com.project.rideEase.repositories.WalletTransactionRepository;
import com.project.rideEase.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
