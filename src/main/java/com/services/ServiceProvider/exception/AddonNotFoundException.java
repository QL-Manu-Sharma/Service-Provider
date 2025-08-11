package com.services.ServiceProvider.exception;

public class AddonNotFoundException extends  RuntimeException{
    private int code;
    public AddonNotFoundException(int code, String message) {

        super(message);
    }
}
