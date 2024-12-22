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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//Total payment = 100
//platform fee = 30

@Service
@RequiredArgsConstructor
@Slf4j
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;



    @Override
    public void processPayment(Payment payment) {

        double paymentAmount = payment.getAmount();
        Driver driver = payment.getRide().getDriver();
        Wallet driverWallet = walletService.findByUser(driver.getUser());
        log.info("Payment amount is: "+paymentAmount);
        double driversCut = paymentAmount*PLATFORM_FEE;
        log.info("Driver's cut is: "+driversCut);
        log.info("Amount remaining after driver's cut: "+(paymentAmount-driversCut));

        Wallet updatedDriverWallet = walletService.deductMoneyFromWallet(driver.getUser(),
                driversCut,null,payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        log.info("Payment is successful!");

    }
}
