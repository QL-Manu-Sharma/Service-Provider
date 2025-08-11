package com.services.ServiceProvider.exception;

public class RequestLimitException extends RuntimeException{
    private int code;
    public RequestLimitException(int code, String message) {

        super(message);
    }
}
