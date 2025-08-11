package com.services.ServiceProvider.exception;

public class TokenException extends RuntimeException{
    private int code;
    private String userName;
    public TokenException(int code, String message) {

        super(message);
    }
}
