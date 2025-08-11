package com.services.ServiceProvider.exception;

public class UserNotFoundException extends RuntimeException{
    private int code;
    private String userName;
    public UserNotFoundException(int code,String userName, String message) {

        super(message);
    }
}