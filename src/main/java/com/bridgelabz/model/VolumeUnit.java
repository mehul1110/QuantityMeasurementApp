package com.bridgelabz.model;

public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    LITER(1.0),
    MILLILITRE(0.001),
    MILLILITER(0.001),
    GALLON(3.78541),
    CUP(0.236588); // 1 gallon ≈ 16 cups? No, 1 cup ≈ 236.588 ml
    
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

    @Override
    public String getMeasurementType() { return "VOLUME"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return VolumeUnit.valueOf(unitName.toUpperCase());
    }
}
