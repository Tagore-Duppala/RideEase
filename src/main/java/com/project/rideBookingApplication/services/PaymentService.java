package com.project.rideBookingApplication.services;

import com.project.rideBookingApplication.entities.Payment;
import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
