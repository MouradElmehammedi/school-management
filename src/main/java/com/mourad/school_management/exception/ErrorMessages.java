package com.mourad.school_management.exception;

public final class ErrorMessages {
    // Messages d'authentification
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access forbidden";
    public static final String AUTHENTICATION_FAILED = "Authentication failed";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String INVALID_TOKEN = "Invalid token";

    // Messages généraux
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String OPERATION_FAILED = "Operation failed";

    // Messages spécifiques aux entités
    public static final String STUDENT_NOT_FOUND = "Student not found";
    public static final String TEACHER_NOT_FOUND = "Teacher not found";
    public static final String PARENT_NOT_FOUND = "Parent not found";
    public static final String CLASSE_NOT_FOUND = "Class not found";
    public static final String SUBJECT_NOT_FOUND = "Subject not found";
    public static final String NOTE_NOT_FOUND = "Note not found";
    public static final String ABSENCE_NOT_FOUND = "Absence not found";
    public static final String SCHEDULE_NOT_FOUND = "Schedule not found";
    public static final String ROLE_NOT_FOUND = "Role not found";

    // Messages de validation
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
    public static final String INVALID_PHONE = "Invalid phone number format";
    public static final String REQUIRED_FIELD = "This field is required";
    public static final String INVALID_NOTE_VALUE = "Note must be between 0 and 20";

    private ErrorMessages() {
        // Empêcher l'instanciation
    }
}
