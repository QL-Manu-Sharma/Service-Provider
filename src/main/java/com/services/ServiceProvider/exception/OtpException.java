package com.services.ServiceProvider.exception;

public class OtpException extends RuntimeException{
    private int code;
    public OtpException(int code, String message) {

        super(message);
    }
}
