package com.project.rideBookingApplication.stratagies.Impl;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Payment;
import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.Wallet;
import com.project.rideBookingApplication.entities.enums.PaymentStatus;
import com.project.rideBookingApplication.entities.enums.TransactionMethod;
import com.project.rideBookingApplication.repositories.PaymentRepository;
import com.project.rideBookingApplication.services.WalletService;
import com.project.rideBookingApplication.stratagies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final WalletService walletService;


    @Override
    public void processPayment(Payment payment) {

        double paymentAmount = payment.getAmount();
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();
        double driversCut = paymentAmount - (paymentAmount*PLATFORM_FEE);

        Wallet riderWallet =  walletService.deductMoneyFromWallet(rider.getUser(),payment.getAmount(),null,
                payment.getRide(), TransactionMethod.RIDE);

        Wallet driverWallet = walletService.addMoneyToWallet(driver.getUser(), driversCut,null,
                payment.getRide(),TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

    }
}
