package com.services.ServiceProvider.exception;

public class ApiException extends Exception{
    public ApiException(String message) {
        super(message);
    }
}