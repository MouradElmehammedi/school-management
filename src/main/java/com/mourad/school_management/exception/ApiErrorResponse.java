package com.mourad.school_management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private int status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String details;
}