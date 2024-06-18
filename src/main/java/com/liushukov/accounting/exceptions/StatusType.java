package com.liushukov.accounting.exceptions;

import org.springframework.http.HttpStatus;

public final class StatusType {
    public final static HttpStatus AUTHENTICATION = HttpStatus.UNAUTHORIZED;
    public final static HttpStatus REGISTRATION = HttpStatus.BAD_REQUEST;
    public final static HttpStatus EXISTED_USER = HttpStatus.CONFLICT;
}
