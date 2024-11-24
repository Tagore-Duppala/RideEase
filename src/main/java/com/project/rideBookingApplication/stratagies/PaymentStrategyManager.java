package com.project.rideBookingApplication.stratagies;

import com.project.rideBookingApplication.entities.Ride;
import com.project.rideBookingApplication.stratagies.Impl.CashPaymentStrategy;
import com.project.rideBookingApplication.stratagies.Impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy paymentMethod(Ride ride){

        return switch (ride.getPaymentMethod()){
            case CASH -> cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
        };

    }

}
