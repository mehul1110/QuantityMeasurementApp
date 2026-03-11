package com.bridgelabz;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {
    CELSIUS(
        celsius -> celsius,                    // toCelsius (identity)
        celsius -> celsius                     // fromCelsius (identity)
    ),
    FAHRENHEIT(
        fahrenheit -> (fahrenheit - 32) * 5.0/9.0,   // F to C
        celsius -> celsius * 9.0/5.0 + 32             // C to F
    );
    
    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;
    
    // Lambda: temperature does NOT support arithmetic
    private final SupportsArithmetic supportsArithmetic = () -> false;
    
    TemperatureUnit(Function<Double,Double> toCelsius, 
                    Function<Double,Double> fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }
    
    // For IMeasurable — temperature uses Celsius as base
    // convertToBaseUnit converts to Celsius
    public double convertToBaseUnit(double value) {
        return toCelsius.apply(value);
    }
    
    // convertFromBaseUnit converts from Celsius to this unit
    public double convertFromBaseUnit(double baseValue) {
        return fromCelsius.apply(baseValue);
    }
    
    // getConversionFactor is not meaningful for temperature
    // return 1.0 as placeholder
    public double getConversionFactor() { return 1.0; }
    
    public String getUnitName() { return this.name(); }
    
    // Override: temperature does NOT support arithmetic
    @Override
    public boolean supportsArithmetic() { return false; }
    
    // Override: throw UnsupportedOperationException for any arithmetic
    @Override
    public void validateOperationSupport(String operationName) {
        throw new UnsupportedOperationException(
            "Temperature does not support " + operationName + 
            ". Temperature arithmetic is not physically meaningful.");
    }
}
