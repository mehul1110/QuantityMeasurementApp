package com.bridgelabz.controller;

import com.bridgelabz.dto.QuantityInputDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;
import com.bridgelabz.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UC17: REST Controller for Quantity Measurement API.
 *
 * Key UC17 Enhancements:
 * - @RestController replaces the plain class that printed to console in UC16.
 * - @RequestMapping("/api/v1/quantities") defines the base URL for all endpoints.
 * - @PostMapping / @GetMapping map HTTP verbs and paths to handler methods.
 * - All methods accept @Valid QuantityInputDTO and return QuantityMeasurementDTO.
 * - Swagger @Tag and @Operation annotations provide automatic API documentation.
 * - No main() method — Spring Boot handles application startup.
 */
@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementController.class);

    /** Spring-injected service — no manual wiring needed */
    @Autowired
    private IQuantityMeasurementService service;

    // =========================================================
    // POST Endpoints: Measurement Operations
    // =========================================================

    /**
     * Compare two quantities for equality.
     *
     * POST /api/v1/quantities/compare
     * Request: { "thisQuantityDTO": {...}, "thatQuantityDTO": {...} }
     * Response: QuantityMeasurementDTO with resultString = "true"/"false"
     */
    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities",
               description = "Returns true if the two quantities are equivalent, false otherwise.")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        log.info("POST /compare called");
        QuantityMeasurementDTO result = service.compare(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    /**
     * Convert a quantity to another unit.
     *
     * POST /api/v1/quantities/convert
     * Request: { "thisQuantityDTO": {...}, "thatQuantityDTO": { unit = target unit } }
     * Response: QuantityMeasurementDTO with resultValue = converted amount
     */
    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to another unit",
               description = "Converts thisQuantityDTO's value into the unit specified by thatQuantityDTO.")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        log.info("POST /convert called");
        QuantityMeasurementDTO result = service.convert(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    /**
     * Add two quantities together. Result is in the unit of thisQuantityDTO.
     *
     * POST /api/v1/quantities/add
     */
    @PostMapping("/add")
    @Operation(summary = "Add two quantities",
               description = "Adds thisQuantityDTO and thatQuantityDTO. Result expressed in thisQuantityDTO's unit.")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        log.info("POST /add called");
        QuantityMeasurementDTO result = service.add(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    /**
     * Subtract thatQuantityDTO from thisQuantityDTO.
     *
     * POST /api/v1/quantities/subtract
     */
    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities",
               description = "Subtracts thatQuantityDTO from thisQuantityDTO. Result expressed in thisQuantityDTO's unit.")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        log.info("POST /subtract called");
        QuantityMeasurementDTO result = service.subtract(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    /**
     * Divide thisQuantityDTO by thatQuantityDTO (dimensionless ratio).
     *
     * POST /api/v1/quantities/divide
     */
    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities",
               description = "Divides thisQuantityDTO by thatQuantityDTO and returns the dimensionless ratio.")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        log.info("POST /divide called");
        QuantityMeasurementDTO result = service.divide(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // =========================================================
    // GET Endpoints: History & Counts
    // =========================================================

    /**
     * Get measurement history for a specific operation type.
     *
     * GET /api/v1/quantities/history/operation/{operation}
     * Example: GET /api/v1/quantities/history/operation/COMPARE
     */
    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get measurement history by operation type",
               description = "Returns all stored measurements for the given operation (e.g., COMPARE, ADD, CONVERT).")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @PathVariable String operation) {
        log.info("GET /history/operation/{} called", operation);
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    /**
     * Get measurement history for a specific measurement type.
     *
     * GET /api/v1/quantities/history/type/{type}
     * Example: GET /api/v1/quantities/history/type/LengthUnit
     */
    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get measurement history by measurement type",
               description = "Returns all stored measurements for the given measurement type (e.g., LengthUnit, WeightUnit).")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementHistory(
            @PathVariable String type) {
        log.info("GET /history/type/{} called", type);
        return ResponseEntity.ok(service.getHistoryByMeasurementType(type));
    }

    /**
     * Get count of successful measurements for a given operation.
     *
     * GET /api/v1/quantities/count/{operation}
     * Example: GET /api/v1/quantities/count/COMPARE
     */
    @GetMapping("/count/{operation}")
    @Operation(summary = "Get successful operation count",
               description = "Returns the number of successful (non-error) measurements for the given operation.")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        log.info("GET /count/{} called", operation);
        return ResponseEntity.ok(service.getCountByOperation(operation));
    }

    /**
     * Get all errored measurement records.
     *
     * GET /api/v1/quantities/history/errored
     */
    @GetMapping("/history/errored")
    @Operation(summary = "Get errored measurements",
               description = "Returns all measurement records that resulted in an error.")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        log.info("GET /history/errored called");
        return ResponseEntity.ok(service.getErrorHistory());
    }
}
