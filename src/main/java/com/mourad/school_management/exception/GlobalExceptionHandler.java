package com.mourad.school_management.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.error("Business error: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication error: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {
        log.error("Bad credentials: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorCode("INVALID_CREDENTIALS")
                .message(ErrorMessages.INVALID_CREDENTIALS)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .errorCode("ACCESS_DENIED")
                .message(ErrorMessages.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.error("Validation errors: {}", errors);
        
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                ErrorMessages.VALIDATION_ERROR,
                LocalDateTime.now(),
                request.getRequestURI(),
                errors
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Illegal argument: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("INVALID_ARGUMENT")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: ", ex);
        
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message(ErrorMessages.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .details("An unexpected error occurred")
                .build();
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}