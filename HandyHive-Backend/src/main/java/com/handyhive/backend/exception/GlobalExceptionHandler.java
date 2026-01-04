package com.handyhive.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException; // ✅ IMPORT THIS
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Not Found (404)
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<?> handleNotFound(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // 2. Bad Request (400) - Validation
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleBadRequest(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            message = "Validation failed: " + ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message);
    }

    // 3. Conflict (409) - Duplicates from Database or Service
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> handleDatabaseConflict(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", "This email is already registered.");
    }

    // 4. Runtime Logic Errors (409 or 400)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        if (ex.getMessage() != null &&
                (ex.getMessage().toLowerCase().contains("conflict") ||
                        ex.getMessage().toLowerCase().contains("already taken"))) {
            return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Request Error", ex.getMessage());
    }

    // ✅ 5. Method Not Allowed (405) - Fixes the "POST not supported" 500 error
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", "Request method '" + ex.getMethod() + "' is not supported for this URL.");
    }

    // 6. Catch-All Server Error (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", "Unexpected: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}