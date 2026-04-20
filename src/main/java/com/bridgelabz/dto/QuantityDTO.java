package com.bridgelabz.dto;

import com.bridgelabz.model.LengthUnit;
import com.bridgelabz.model.TemperatureUnit;
import com.bridgelabz.model.VolumeUnit;
import com.bridgelabz.model.WeightUnit;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * UC17: Refactored QuantityDTO with String-based unit and measurementType fields.
 *
 * Used in API requests to represent a single quantity.
 * Replaces the inner-enum approach from UC16.
 */
public class QuantityDTO {

    @NotNull(message = "Value cannot be null")
    private Double value;

    @NotEmpty(message = "Unit cannot be empty")
    private String unit;

    @NotEmpty(message = "Measurement type cannot be empty")
    @Pattern(
            regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
            message = "Measurement type must be one of: LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit"
    )
    private String measurementType;

    public QuantityDTO() {}

    public QuantityDTO(Double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    /**
     * Cross-field validation: ensures unit is valid for the given measurementType.
     * @AssertTrue — Bean Validation calls this; if false, raises a validation error.
     */
    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isUnitValidForMeasurementType() {
        if (unit == null || measurementType == null) return true;
        try {
            switch (measurementType) {
                case "LengthUnit":      LengthUnit.valueOf(unit.toUpperCase());      return true;
                case "WeightUnit":      WeightUnit.valueOf(unit.toUpperCase());      return true;
                case "VolumeUnit":      VolumeUnit.valueOf(unit.toUpperCase());      return true;
                case "TemperatureUnit": TemperatureUnit.valueOf(unit.toUpperCase()); return true;
                default:                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getMeasurementType() { return measurementType; }
    public void setMeasurementType(String measurementType) { this.measurementType = measurementType; }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
