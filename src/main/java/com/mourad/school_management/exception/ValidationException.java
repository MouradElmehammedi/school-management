package com.mourad.school_management.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
} 