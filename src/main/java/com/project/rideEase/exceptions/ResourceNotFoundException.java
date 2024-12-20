package com.project.rideEase.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
