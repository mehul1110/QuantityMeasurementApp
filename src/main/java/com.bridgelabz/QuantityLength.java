package com.bridgelabz;

public class QuantityLength {

    // Enum defining supported length units and their conversion factors relative to FEET
    public enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(1.0 / 30.48);         // 1 cm = 1/30.48 feet

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityLength other = (QuantityLength) obj;
        double thisFeet  = this.value  * this.unit.getConversionFactor();
        double otherFeet = other.value * other.unit.getConversionFactor();
        return Double.compare(thisFeet, otherFeet) == 0;
    }

    @Override
    public String toString() {
        return value + " " + unit.name();
    }
}
