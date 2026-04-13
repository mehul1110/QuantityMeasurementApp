package com.bridgelabz;

public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);
    
    private final double conversionFactor;
    
    VolumeUnit(double conversionFactor) { 
        this.conversionFactor = conversionFactor; 
    }
    
    public double getConversionFactor() { return conversionFactor; }
    public double convertToBaseUnit(double value) { 
        return value * conversionFactor; 
    }
    public double convertFromBaseUnit(double baseValue) { 
        return baseValue / conversionFactor; 
    }
    public String getUnitName() { return this.name(); }
}
