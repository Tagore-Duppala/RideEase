package com.project.rideBookingApplication.services.Impl;

import com.project.rideBookingApplication.dto.DriverDto;
import com.project.rideBookingApplication.dto.SignUpDto;
import com.project.rideBookingApplication.dto.UserDto;
import com.project.rideBookingApplication.entities.Driver;
import com.project.rideBookingApplication.entities.Rider;
import com.project.rideBookingApplication.entities.User;
import com.project.rideBookingApplication.entities.enums.Role;
import com.project.rideBookingApplication.exceptions.ResourceNotFoundException;
import com.project.rideBookingApplication.exceptions.RuntimeConflictException;
import com.project.rideBookingApplication.repositories.UserRepository;
import com.project.rideBookingApplication.security.JWTService;
import com.project.rideBookingApplication.services.AuthService;
import com.project.rideBookingApplication.services.DriverService;
import com.project.rideBookingApplication.services.RiderService;
import com.project.rideBookingApplication.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.project.rideBookingApplication.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignUpDto signUpDto) {
         User user1 = userRepository.findByEmail(signUpDto.getEmail()).orElse(null);
         if(user1!=null) throw new
                 ResourceNotFoundException("User already exist with email "+ signUpDto.getEmail());

        User user = modelMapper.map(signUpDto,User.class);
        user.setRoles(Set.of(Role.RIDER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user); //saving details in user entity
        Rider rider=riderService.createNewRider(user); //create new rider, saving in rider entity

        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with User Id: "+userId));

        if(user.getRoles().contains(DRIVER)) throw new RuntimeConflictException("User is already onboarded as DRIVER");
        user.getRoles().add(DRIVER);
        Driver driver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .build();

        Driver savedDriver = driverService.createNewDriver(driver);
        userRepository.save(user);

        return modelMapper.map(savedDriver,DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {

        Long userId = jwtService.generateUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with User id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
