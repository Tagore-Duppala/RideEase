package com.project.rideBookingApplication.controllers;

import com.project.rideBookingApplication.dto.DriverDto;
import com.project.rideBookingApplication.dto.OnboardNewDriverDto;
import com.project.rideBookingApplication.dto.SignUpDto;
import com.project.rideBookingApplication.dto.UserDto;
import com.project.rideBookingApplication.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthContorller {

    private final AuthService authService;

    @PostMapping(path = "/signup")
    public UserDto signUp(@RequestBody SignUpDto signUpDto){
        return authService.signup(signUpDto);
    }

    @PostMapping("/onboardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onboardNewDriver(@PathVariable Long userId, @RequestBody OnboardNewDriverDto onboardNewDriverDto){
        return ResponseEntity.ok(authService.onboardNewDriver(userId,onboardNewDriverDto.getVehicleId()));
    }

}
