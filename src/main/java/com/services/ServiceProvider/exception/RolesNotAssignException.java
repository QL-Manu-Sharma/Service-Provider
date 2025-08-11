package com.services.ServiceProvider.exception;

public class RolesNotAssignException extends RuntimeException{
    private int code;
    public RolesNotAssignException(int code, String message) {

        super(message);
    }
}
