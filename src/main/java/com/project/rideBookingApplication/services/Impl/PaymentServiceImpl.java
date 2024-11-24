package com.project.rideBookingApplication.services.Impl;

import com.project.rideBookingApplication.entities.Payment;
import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.enums.PaymentStatus;
import com.project.rideBookingApplication.repositories.PaymentRepository;
import com.project.rideBookingApplication.services.PaymentService;
import com.project.rideBookingApplication.stratagies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride);
        paymentStrategyManager.paymentMethod(ride).processPayment(payment);

    }

    @Override
    public Payment createPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(ride.getFare())
                .paymentMethod(ride.getPaymentMethod())
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
