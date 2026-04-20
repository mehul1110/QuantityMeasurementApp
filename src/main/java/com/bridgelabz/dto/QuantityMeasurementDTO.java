package com.bridgelabz.dto;

import com.bridgelabz.model.QuantityMeasurementEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC17: Data Transfer Object for quantity measurement API responses.
 *
 * Contains static factory methods for entity <-> DTO conversion.
 */
public class QuantityMeasurementDTO {

    private Double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    private String operation;

    private String resultString;
    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    private String errorMessage;
    private boolean error;

    public QuantityMeasurementDTO() {}

    public QuantityMeasurementDTO(Double thisValue, String thisUnit, String thisMeasurementType,
                                   Double thatValue, String thatUnit, String thatMeasurementType,
                                   String operation, String resultString, Double resultValue,
                                   String resultUnit, String resultMeasurementType,
                                   String errorMessage, boolean error) {
        this.thisValue = thisValue;
        this.thisUnit = thisUnit;
        this.thisMeasurementType = thisMeasurementType;
        this.thatValue = thatValue;
        this.thatUnit = thatUnit;
        this.thatMeasurementType = thatMeasurementType;
        this.operation = operation;
        this.resultString = resultString;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.resultMeasurementType = resultMeasurementType;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    // =========================================================
    // Static Factory Methods
    // =========================================================

    /**
     * Converts a QuantityMeasurementEntity to a QuantityMeasurementDTO.
     */
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(entity.getThisValue());
        dto.setThisUnit(entity.getThisUnit());
        dto.setThisMeasurementType(entity.getThisMeasurementType());
        dto.setThatValue(entity.getThatValue());
        dto.setThatUnit(entity.getThatUnit());
        dto.setThatMeasurementType(entity.getThatMeasurementType());
        dto.setOperation(entity.getOperation());
        dto.setResultString(entity.getResultString());
        dto.setResultValue(entity.getResultValue());
        dto.setResultUnit(entity.getResultUnit());
        dto.setResultMeasurementType(entity.getResultMeasurementType());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setError(entity.isError());
        return dto;
    }

    /**
     * Converts this DTO to a QuantityMeasurementEntity.
     */
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(this.thisValue);
        entity.setThisUnit(this.thisUnit);
        entity.setThisMeasurementType(this.thisMeasurementType);
        entity.setThatValue(this.thatValue);
        entity.setThatUnit(this.thatUnit);
        entity.setThatMeasurementType(this.thatMeasurementType);
        entity.setOperation(this.operation);
        entity.setResultString(this.resultString);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setResultMeasurementType(this.resultMeasurementType);
        entity.setErrorMessage(this.errorMessage);
        entity.setError(this.error);
        return entity;
    }

    /**
     * Converts a list of entities to DTOs using Java Stream API.
     */
    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of DTOs to entities using Java Stream API.
     */
    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }

    // =========================================================
    // Getters and Setters
    // =========================================================

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
}
