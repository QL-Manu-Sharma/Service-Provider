package com.services.ServiceProvider.exception;

public class TokenNotCreatedException extends RuntimeException{

    private int code;

    private boolean status;

    public TokenNotCreatedException(int code, String message){
        super(message);
        this.code=code;

    }
}