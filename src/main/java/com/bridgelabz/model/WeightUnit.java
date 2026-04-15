package com.bridgelabz.model;

/**
 * Standalone enum representing weight units.
 * Each constant knows its conversion factor relative to KILOGRAM (the base unit).
 */
public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592),
    OUNCE(0.453592 / 16.0), // 1 lb = 16 oz
    TONNE(1000.0);

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

    @Override
    public String getMeasurementType() { return "WEIGHT"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return WeightUnit.valueOf(unitName.toUpperCase());
    }
}
