package com.bridgelabz.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * UC17: Global Exception Handler for REST Controllers.
 *
 * Uses @ControllerAdvice to intercept exceptions thrown by any controller
 * and returns consistent, structured error responses using ErrorResponse.
 *
 * Handles three levels of exceptions:
 * 1. handleMethodArgumentNotValidException — Bean Validation errors (400)
 * 2. handleQuantityException              — Domain-specific errors (400)
 * 3. handleGlobalException                — Any unhandled exception (500)
 *
 * Benefits:
 * - Separates error handling logic from controller code (SRP).
 * - Clients receive consistent and informative error messages.
 * - Avoids verbose stack traces reaching the API consumer.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors thrown when request body fails @Valid / Bean Validation constraints.
     *
     * Triggered when @Valid on @RequestBody fails (e.g., invalid unit name,
     * missing required fields, @AssertTrue returns false).
     *
     * @param ex      the MethodArgumentNotValidException containing binding result
     * @param request the incoming HTTP request
     * @return 400 Bad Request with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Extract the first field error message, or a generic message
        String message = ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> ((FieldError) error).getDefaultMessage())
                .findFirst()
                .orElse(ex.getBindingResult().getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .findFirst()
                        .orElse("Validation failed"));

        // Use field name as path for field errors, or "quantityInputDTO" for object-level errors
        String path = ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> ((FieldError) error).getField())
                .findFirst()
                .orElse("quantityInputDTO");

        log.warn("Validation error on {}: {}", path, message);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Quantity Measurement Error",
                message,
                path
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles domain-specific exceptions from the service layer.
     *
     * Triggered by QuantityMeasurementException (e.g., incompatible measurement types,
     * invalid operation, or domain rule violations).
     *
     * @param ex      the QuantityMeasurementException
     * @param request the incoming HTTP request
     * @return 400 Bad Request with domain error details
     */
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<ErrorResponse> handleQuantityException(
            QuantityMeasurementException ex,
            HttpServletRequest request) {

        log.warn("Quantity measurement error on {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Quantity Measurement Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Catch-all handler for any other unexpected exceptions.
     *
     * Provides a generic Internal Server Error response, preventing
     * sensitive stack trace information from leaking to the client.
     *
     * @param ex      the unhandled Exception
     * @param request the incoming HTTP request
     * @return 500 Internal Server Error with generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
