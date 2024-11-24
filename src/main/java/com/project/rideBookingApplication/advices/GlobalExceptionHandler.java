package com.project.rideBookingApplication.advices;

import com.project.rideBookingApplication.exceptions.ResourceNotFoundException;
import com.project.rideBookingApplication.exceptions.RuntimeConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiError> handleRuntimeConflict(RuntimeConflictException runtimeConflictException){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(runtimeConflictException.getMessage())
                .createdTimestamp(new Date())
                .build();
        return new ResponseEntity<ApiError>(apiError,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleRuntimeConflict(ResourceNotFoundException resourceNotFoundException){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(resourceNotFoundException.getMessage())
                .createdTimestamp(new Date())
                .build();
        return new ResponseEntity<ApiError>(apiError,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalErrors(Exception exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .createdTimestamp(new Date())
                .build();
        return new ResponseEntity<ApiError>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
