package com.example.jbooking.exception;

public class NoAvailabilityException extends ConflictException {
    public NoAvailabilityException(String message) {
        super(message);
    }
}