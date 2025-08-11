package com.services.ServiceProvider.exception;

import com.services.ServiceProvider.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.NoSuchFileException;
import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage()).data(Collections.EMPTY_MAP).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorString = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorString.append(fieldName).append(" ").append(errorMessage).append(",");

        });
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(451).data(Map.of("detailedError",errorString)).build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex){
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(456).data(Map.of("detailedError",ex.getMessage())).build(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public final ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(460).data(Map.of("detailedError",ex.getMessage())).build(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NoSuchFileException.class)
    public final ResponseEntity<ApiResponse> handleNoSuchFileException(NoSuchFileException ex) {
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(462).data(Map.of("detailedError",ex.getMessage())).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<ApiResponse> handleApiException(ApiException ex){
        return new ResponseEntity<>(ApiResponse.builder().status(false).code(466).data(Map.of("detailedError",ex.getMessage())).build(), HttpStatus.BAD_REQUEST);
    }

}