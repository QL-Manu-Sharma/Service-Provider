package com.services.ServiceProvider.exception;

public class ServiceNotFoundException extends RuntimeException{
    private int code;
    public ServiceNotFoundException(int code, String message) {

        super(message);
    }
}
