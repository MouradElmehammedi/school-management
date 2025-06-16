package com.mourad.school_management.exception;


public class ErrorMessages {
    // Messages d'authentification
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access forbidden";

    // Messages généraux
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String VALIDATION_ERROR = "Validation error";

    // Messages spécifiques aux entités
    public static final String STUDENT_NOT_FOUND = "Student not found";
    public static final String TEACHER_NOT_FOUND = "Teacher not found";
    public static final String PARENT_NOT_FOUND = "Parent not found";
    public static final String CLASSE_NOT_FOUND = "Class not found";
    public static final String SUBJECT_NOT_FOUND = "Subject not found";
    public static final String NOTE_NOT_FOUND = "Note not found";
    public static final String ABSENCE_NOT_FOUND = "Absence not found";
    public static final String SCHEDULE_NOT_FOUND = "Schedule not found";
}
