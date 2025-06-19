package com.mourad.school_management.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BUSINESS_ERROR");
    }
}
