package com.services.ServiceProvider.exception;

public class UserExistsException extends RuntimeException{
    private int code;
    public UserExistsException(int code, String message) {

        super(message);
    }
}
