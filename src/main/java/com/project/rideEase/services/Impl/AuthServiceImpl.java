package com.project.rideEase.services.Impl;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.SignUpDto;
import com.project.rideEase.dto.UserDto;
import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.Rider;
import com.project.rideEase.entities.User;
import com.project.rideEase.entities.enums.Role;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.exceptions.RuntimeConflictException;
import com.project.rideEase.repositories.UserRepository;
import com.project.rideEase.security.JWTService;
import com.project.rideEase.services.AuthService;
import com.project.rideEase.services.DriverService;
import com.project.rideEase.services.RiderService;
import com.project.rideEase.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.project.rideEase.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final DriverService driverService;

    @Override
    public String[] login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return new String[]{accessToken, refreshToken};
        }
        catch (Exception ex){
            log.error("Exception occurred while logging in, Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Error occurred while logging in, Error Msg: "+ex.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDto signup(SignUpDto signUpDto) {

        try {
            User user1 = userRepository.findByEmail(signUpDto.getEmail()).orElse(null);
            if (user1 != null) throw new
                    ResourceNotFoundException("User already exist with email " + signUpDto.getEmail());

            log.info("Signing up new user!");
            User user = modelMapper.map(signUpDto, User.class);
            user.setRoles(Set.of(Role.RIDER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user); //saving details in user entity
            Rider rider = riderService.createNewRider(user); //create new rider, saving in rider entity

            walletService.createNewWallet(savedUser);

            return modelMapper.map(savedUser, UserDto.class);
        }
        catch (Exception ex){
            log.error("Exception occurred in signup, Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in signup, Error Msg: "+ ex.getMessage());
        }
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId){

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with User Id: " + userId));

            if (user.getRoles().contains(DRIVER))
                throw new RuntimeConflictException("User is already onboarded as DRIVER");
            user.getRoles().add(DRIVER);
            Driver driver = Driver.builder()
                    .user(user)
                    .rating(0.0)
                    .vehicleId(vehicleId)
                    .available(true)
                    .build();

            Driver savedDriver = driverService.createNewDriver(driver);
            userRepository.save(user);

            return modelMapper.map(savedDriver, DriverDto.class);
        }
        catch (Exception ex){
            log.error("Exception occurred while onboarding new driver, Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred while onboarding new driver, Error Msg: "+ ex.getMessage());
        }
    }

    @Override
    public String refreshToken(String refreshToken) {

        Long userId = jwtService.generateUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with User id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
