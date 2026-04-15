package com.bridgelabz.model;

/**
 * Standalone enum representing length units.
 * Each constant knows its conversion factor relative to FEET (the base unit)
 * and can directly perform conversions — keeping all conversion math in one place.
 */
public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    YARD(3.0),
    CENTIMETERS(1.0 / 30.48),
    CENTIMETER(1.0 / 30.48),
    METER(1.0 / 0.3048); // 1 meter ≈ 3.28084 feet

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    /** Returns the factor to multiply by when converting a value of this unit to feet. */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /** Convert a value expressed in this unit to the base unit (feet). */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /** Convert a value expressed in the base unit (feet) to this unit. */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    public String getUnitName() {
        return this.name();
    }

    @Override
    public String getMeasurementType() { return "LENGTH"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return LengthUnit.valueOf(unitName.toUpperCase());
    }
}
