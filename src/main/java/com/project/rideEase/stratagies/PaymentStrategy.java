package com.project.rideEase.stratagies;

import com.project.rideEase.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_FEE = 0.3;

    void processPayment(Payment payment);

}
