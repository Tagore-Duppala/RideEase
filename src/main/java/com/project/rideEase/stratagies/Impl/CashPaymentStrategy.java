package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Payment;
import com.project.rideEase.entities.Wallet;
import com.project.rideEase.entities.enums.PaymentStatus;
import com.project.rideEase.entities.enums.TransactionMethod;
import com.project.rideEase.repositories.PaymentRepository;
import com.project.rideEase.services.WalletService;
import com.project.rideEase.stratagies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//Total payment = 100
//platform fee = 30

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;



    @Override
    public void processPayment(Payment payment) {

        double paymentAmount = payment.getAmount();
        Driver driver = payment.getRide().getDriver();
        Wallet driverWallet = walletService.findByUser(driver.getUser());
        double driversCut = paymentAmount - (paymentAmount*PLATFORM_FEE);

        Wallet updatedDriverWallet = walletService.deductMoneyFromWallet(driver.getUser(),
                driversCut,null,payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

    }
}
