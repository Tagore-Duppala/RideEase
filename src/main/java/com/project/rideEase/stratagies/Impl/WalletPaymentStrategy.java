package com.project.rideEase.stratagies.Impl;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Payment;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.Wallet;
import com.project.rideEase.entities.enums.PaymentStatus;
import com.project.rideEase.entities.enums.TransactionMethod;
import com.project.rideEase.repositories.PaymentRepository;
import com.project.rideEase.services.WalletService;
import com.project.rideEase.stratagies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final WalletService walletService;


    @Override
    public void processPayment(Payment payment) {

        try {
            double paymentAmount = payment.getAmount();
            Driver driver = payment.getRide().getDriver();
            Rider rider = payment.getRide().getRider();
            double driversCut = paymentAmount - (paymentAmount * PLATFORM_FEE);

            Wallet riderWallet = walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null,
                    payment.getRide(), TransactionMethod.RIDE);

            Wallet driverWallet = walletService.addMoneyToWallet(driver.getUser(), driversCut, null,
                    payment.getRide(), TransactionMethod.RIDE);

            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);
        } catch (Exception ex) {
            log.error("Exception occurred in processPayment , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in processPayment: "+ex.getMessage());
        }

    }
}
