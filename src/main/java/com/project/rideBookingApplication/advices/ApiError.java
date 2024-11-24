package com.project.rideBookingApplication.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    private Date createdTimestamp;
}
