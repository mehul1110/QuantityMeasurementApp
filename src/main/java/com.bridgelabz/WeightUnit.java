package com.bridgelabz;

/**
 * Standalone enum representing weight units.
 * Each constant knows its conversion factor relative to KILOGRAM (the base unit).
 */
public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),          // 1g = 0.001 kg
    POUND(0.453592);      // 1 lb ≈ 0.453592 kg

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    /** Returns the factor to multiply by when converting a value of this unit to kg. */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /** Convert a value expressed in this unit to the base unit (kilograms). */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /** Convert a value expressed in the base unit (kilograms) to this unit. */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    public String getUnitName() {
        return this.name();
    }
}
