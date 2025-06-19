package com.mourad.school_management.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ApiErrorResponse {
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(int status, String errorCode, String message, 
                                 LocalDateTime timestamp, String path, Map<String, String> fieldErrors) {
        super(status, errorCode, message, timestamp, path, null, null);
        this.fieldErrors = fieldErrors;
    }
}