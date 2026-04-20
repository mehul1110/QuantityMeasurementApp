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
 * UC17: Global Exception Handler.
 *
 * @ControllerAdvice — intercepts exceptions from ALL @RestController classes.
 * All error responses follow the consistent ErrorResponse structure.
 *
 * Three levels handled:
 *  1. MethodArgumentNotValidException — Bean Validation failures (@Valid)  → 400
 *  2. QuantityMeasurementException   — Domain / business rule violations   → 400
 *  3. Exception                      — Any unexpected runtime error         → 500
 *
 * Benefits:
 * - Controllers stay clean — no try/catch for HTTP error mapping
 * - Clients get consistent, structured error messages across all endpoints
 * - Logging is centralised here, not scattered across controllers
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles Bean Validation failures thrown when @Valid on @RequestBody fails.
     *
     * Message format: "fieldName: validation message"
     * (e.g., "thatQuantityDTO.unit: Unit cannot be empty")
     *
     * @param ex      MethodArgumentNotValidException from Spring Validation
     * @param request the incoming HTTP request (provides URI for path field)
     * @return 400 Bad Request with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Format: "fieldName: message" — gives the client exactly which field failed and why
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst()
                .orElseGet(() -> ex.getBindingResult().getAllErrors()
                        .stream()
                        .map(e -> e.getDefaultMessage())
                        .findFirst()
                        .orElse("Validation failed"));

        // Path = failed field name for field errors, or request URI for object-level errors
        String path = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .findFirst()
                .orElse(request.getRequestURI());

        log.warn("Validation error [{}] on path [{}]: {}", request.getMethod(), path, message);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                path
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles domain-specific business exceptions from the service layer.
     *
     * Examples: incompatible measurement types, invalid unit names,
     * temperature arithmetic attempted, unknown measurement type.
     *
     * @param ex      QuantityMeasurementException with business rule message
     * @param request the incoming HTTP request
     * @return 400 Bad Request with domain error details
     */
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<ErrorResponse> handleQuantityException(
            QuantityMeasurementException ex,
            HttpServletRequest request) {

        log.warn("Domain error [{}] on [{}]: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());

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
     * Catch-all: handles any unexpected exception not caught by the above handlers.
     *
     * Returns a generic 500 response. Stack trace is logged server-side
     * but NOT exposed to the API consumer (security best practice).
     *
     * @param ex      the unhandled Exception
     * @param request the incoming HTTP request
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error [{}] on [{}]: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

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
