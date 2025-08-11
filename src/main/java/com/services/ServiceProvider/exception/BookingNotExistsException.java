package com.services.ServiceProvider.exception;

public class BookingNotExistsException extends RuntimeException{
    private int code;
    public BookingNotExistsException(int code, String message) {

        super(message);
    }
}
