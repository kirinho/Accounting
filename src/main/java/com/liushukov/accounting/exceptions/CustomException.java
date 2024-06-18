package com.liushukov.accounting.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception{
    private final HttpStatus httpStatus;
    public CustomException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
