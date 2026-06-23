package com.handyhive.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<?> handleNotFound(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<?> handleBadRequest(Exception ex, WebRequest request) {
        String message = ex.getMessage();

        if (ex instanceof MethodArgumentNotValidException manv && manv.getBindingResult().getFieldError() != null) {
            message = "Validation failed: " + manv.getBindingResult().getFieldError().getDefaultMessage();
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "Invalid or missing JSON request body.";
        } else if (ex instanceof MissingServletRequestParameterException missing) {
            message = "Missing required request parameter: " + missing.getParameterName();
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message);
    }

    // ✅ 415 - invalid content type (fixes your current 500)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        String supported = (ex.getSupportedMediaTypes() == null || ex.getSupportedMediaTypes().isEmpty())
                ? ""
                : " Supported: " + ex.getSupportedMediaTypes();
        return buildResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type",
                "Content-Type '" + ex.getContentType() + "' is not supported." + supported
        );
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method Not Allowed",
                "Request method '" + ex.getMethod() + "' is not supported for this URL."
        );
    }

    // 409 - DB constraint violations (make message generic, not “email” only)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabaseConflict(DataIntegrityViolationException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", "Database constraint violation (duplicate or invalid reference).");
    }

    // 400/409 from your own runtime checks
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        if (ex.getMessage() != null) {
            String m = ex.getMessage().toLowerCase();
            if (m.contains("conflict") || m.contains("already")) {
                return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
            }
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Request Error", ex.getMessage());
    }

    // 500 only for real server problems
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Server Error",
                "Unexpected: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()
        );
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
