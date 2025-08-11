package com.services.ServiceProvider.exception;

public class ProviderNotFoundException extends RuntimeException{
    private int code;
    public ProviderNotFoundException(int code, String message) {

        super(message);
    }
}
