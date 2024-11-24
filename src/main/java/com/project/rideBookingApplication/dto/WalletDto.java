package com.project.rideBookingApplication.dto;

import com.project.rideBookingApplication.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {

    private Long id;

    private User user;

    private Double balance;

    private List<WalletTransactionDto> transactions;
}
