package com.project.rideEase.controllers;

import com.project.rideEase.dto.*;
import com.project.rideEase.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @Secured("ROLE_ADMIN")
    @PostMapping("/onboardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onboardNewDriver(@PathVariable Long userId, @RequestBody OnboardNewDriverDto onboardNewDriverDto){
        return ResponseEntity.ok(authService.onboardNewDriver(userId,onboardNewDriverDto.getVehicleId()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto
                                                    , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String[] tokens = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String accessToken = tokens[0];
        Cookie cookie = new Cookie("refreshToken",tokens[1]);
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));

    }

}
