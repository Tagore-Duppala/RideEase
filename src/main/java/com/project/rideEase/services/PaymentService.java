package com.project.rideEase.services;

import com.project.rideEase.entities.Payment;
import com.project.rideEase.entities.Ride;
import com.project.rideEase.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
