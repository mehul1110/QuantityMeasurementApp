package com.bridgelabz.model;

public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }
    
    SupportsArithmetic supportsArithmetic = () -> true;
    
    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }
    
    default void validateOperationSupport(String operationName) {
        // default: all operations supported, do nothing
    }

    String getMeasurementType();
    IMeasurable getUnitByName(String unitName);
}
