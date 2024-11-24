package com.project.rideBookingApplication.stratagies;

import com.project.rideBookingApplication.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_FEE = 0.3;

    void processPayment(Payment payment);

}
