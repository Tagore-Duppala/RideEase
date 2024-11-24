package com.project.rideBookingApplication.stratagies.Impl;

import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Payment;
import com.project.rideBookingApplication.entities.Wallet;
import com.project.rideBookingApplication.entities.enums.PaymentStatus;
import com.project.rideBookingApplication.entities.enums.TransactionMethod;
import com.project.rideBookingApplication.repositories.PaymentRepository;
import com.project.rideBookingApplication.services.WalletService;
import com.project.rideBookingApplication.stratagies.PaymentStrategy;
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
