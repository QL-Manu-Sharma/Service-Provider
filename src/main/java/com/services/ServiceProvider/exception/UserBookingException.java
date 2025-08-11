package com.services.ServiceProvider.exception;

public class UserBookingException extends RuntimeException{
    private int code;
    public UserBookingException(int code, String message) {

        super(message);
    }
}
