package com.project.rideEase.controllers;

import com.project.rideEase.dto.DriverDto;
import com.project.rideEase.dto.RideDto;
import com.project.rideEase.dto.RideRequestDto;
import com.project.rideEase.dto.RiderDto;
import com.project.rideEase.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto){
        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));

    }

    @PostMapping("/cancelRide/{rideRequestId}")
    public  ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }

    @PostMapping("/cancelRideRequest/{rideRequestId}")
    public  ResponseEntity<RideRequestDto> cancelRideRequest(@PathVariable Long rideRequestId){
        return ResponseEntity.ok(riderService.cancelRideRequest(rideRequestId));
    }

    @GetMapping("/getMyProfile")
    public  ResponseEntity<RiderDto> getMyProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @PostMapping("/rateDriver/{rideId}/{rating}")
    public ResponseEntity<DriverDto> rateRider(Long rideId, @PathVariable Double rating){
        return  ResponseEntity.ok(riderService.rateDriver(rideId, rating));
    }

    @GetMapping("/getAllMyRides")
    public  ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                        @RequestParam(defaultValue = "10") Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset,pageSize);
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }

}
