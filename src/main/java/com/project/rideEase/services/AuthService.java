package com.project.rideEase.services;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.SignUpDto;
import com.project.rideEase.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignUpDto signUpDto);

    DriverDto onboardNewDriver(Long UserId, String vehicleId);

    String refreshToken(String refreshToken);

}
