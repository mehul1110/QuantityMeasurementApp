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

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
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

    /**
     * Converts this QuantityLength to the target unit.
     * Returns a NEW QuantityLength object (immutable).
     *
     * @param targetUnit the unit to convert to; must not be null
     * @return new QuantityLength in the target unit, rounded to 6 decimal places
     * @throws IllegalArgumentException if targetUnit is null, or value is NaN/Infinite
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        if (Double.isNaN(this.value) || Double.isInfinite(this.value))
            throw new IllegalArgumentException("Value must be finite");

        double baseValue = this.value * this.unit.getConversionFactor();      // to feet
        double result    = baseValue  / targetUnit.getConversionFactor();      // to target

        // Round to 6 decimal places
        double factor = 1_000_000.0;
        result = Math.round(result * factor) / factor;

        return new QuantityLength(result, targetUnit);
    }

    /**
     * Helper: converts both operands to feet and returns their sum in feet.
     *
     * @param other the other QuantityLength to add; must not be null and must be finite
     * @return sum of both lengths expressed in feet (base unit)
     * @throws IllegalArgumentException if other is null or other's value is not finite
     */
    private double addToBaseUnit(QuantityLength other) {
        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");
        if (Double.isNaN(other.value) || Double.isInfinite(other.value))
            throw new IllegalArgumentException("Operand value must be finite");

        double thisBase  = this.value  * this.unit.getConversionFactor();
        double otherBase = other.value * other.unit.getConversionFactor();
        return thisBase + otherBase;
    }

    /**
     * Adds another QuantityLength to this one.
     * Result is in THIS object's unit (delegates to add(other, this.unit)).
     */
    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    /**
     * Adds another QuantityLength to this one, expressed in an explicit target unit.
     * Returns a NEW QuantityLength (immutable — originals unchanged).
     *
     * @param other      the QuantityLength to add; must not be null and must be finite
     * @param targetUnit the unit of the result; must not be null
     * @return new QuantityLength in targetUnit, rounded to 2 decimal places
     * @throws IllegalArgumentException if targetUnit is null, other is null, or other is non-finite
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseSum = addToBaseUnit(other);                               // sum in feet
        double result  = baseSum / targetUnit.getConversionFactor();         // convert to targetUnit

        // Round to 2 decimal places
        double factor = 100.0;
        result = Math.round(result * factor) / factor;

        return new QuantityLength(result, targetUnit);
    }
}

