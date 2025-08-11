package com.services.ServiceProvider.exception;

public class LogoutFailedException extends  RuntimeException{
    private int code;
    public LogoutFailedException(int code, String message) {

        super(message);
    }
}
