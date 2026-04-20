package com.bridgelabz.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * UC17: JPA-annotated entity for quantity measurement operations.
 *
 * Replaces the plain POJO from UC16 (entity/QuantityMeasurementEntity.java).
 * JPA annotations map this class to the `quantity_measurements` database table.
 */
@Entity
@Table(
        name = "quantity_measurements",
        indexes = {
                @Index(name = "idx_operation",        columnList = "operation"),
                @Index(name = "idx_measurement_type", columnList = "thisMeasurementType"),
                @Index(name = "idx_is_error",         columnList = "error")
        }
)
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double thisValue;

    @Column(nullable = false)
    private String thisUnit;

    @Column(nullable = false)
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    @Column(nullable = false)
    private String operation;

    private String resultString;
    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    private String errorMessage;

    @Column(name = "error", nullable = false)
    private boolean error = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public QuantityMeasurementEntity() {}

    /**
     * Automatically sets timestamps on entity creation.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically updates the updatedAt timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // =========================================================
    // Getters and Setters
    // =========================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getThisValue() { return thisValue; }
    public void setThisValue(Double thisValue) { this.thisValue = thisValue; }

    public String getThisUnit() { return thisUnit; }
    public void setThisUnit(String thisUnit) { this.thisUnit = thisUnit; }

    public String getThisMeasurementType() { return thisMeasurementType; }
    public void setThisMeasurementType(String thisMeasurementType) { this.thisMeasurementType = thisMeasurementType; }

    public Double getThatValue() { return thatValue; }
    public void setThatValue(Double thatValue) { this.thatValue = thatValue; }

    public String getThatUnit() { return thatUnit; }
    public void setThatUnit(String thatUnit) { this.thatUnit = thatUnit; }

    public String getThatMeasurementType() { return thatMeasurementType; }
    public void setThatMeasurementType(String thatMeasurementType) { this.thatMeasurementType = thatMeasurementType; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getResultString() { return resultString; }
    public void setResultString(String resultString) { this.resultString = resultString; }

    public Double getResultValue() { return resultValue; }
    public void setResultValue(Double resultValue) { this.resultValue = resultValue; }

    public String getResultUnit() { return resultUnit; }
    public void setResultUnit(String resultUnit) { this.resultUnit = resultUnit; }

    public String getResultMeasurementType() { return resultMeasurementType; }
    public void setResultMeasurementType(String resultMeasurementType) { this.resultMeasurementType = resultMeasurementType; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
