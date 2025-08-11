package com.services.ServiceProvider.exception;

public class ExpiredTokenException extends  RuntimeException{
    private int code;
    private String userName;
    public ExpiredTokenException(int code,String userName, String message) {

        super(message);
    }
}
