package com.bridgelabz;

/**
 * Immutable value object representing a length measurement.
 * All unit-conversion math is fully delegated to LengthUnit.
 */
public class QuantityLength {

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
        double thisFeet  = this.unit.convertToBaseUnit(this.value);
        double otherFeet = other.unit.convertToBaseUnit(other.value);
        return Double.compare(thisFeet, otherFeet) == 0;
    }

    @Override
    public String toString() {
        return value + " " + unit.name();
    }

    /**
     * Converts this QuantityLength to the target unit.
     * Returns a NEW QuantityLength object (immutable).
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        if (Double.isNaN(this.value) || Double.isInfinite(this.value))
            throw new IllegalArgumentException("Value must be finite");

        double base   = this.unit.convertToBaseUnit(this.value);
        double result = targetUnit.convertFromBaseUnit(base);

        double factor = 1_000_000.0;
        result = Math.round(result * factor) / factor;

        return new QuantityLength(result, targetUnit);
    }

    /**
     * Helper: converts both operands to the base unit (feet) and returns their sum.
     */
    private double addToBaseUnit(QuantityLength other) {
        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");
        if (Double.isNaN(other.value) || Double.isInfinite(other.value))
            throw new IllegalArgumentException("Operand value must be finite");

        return this.unit.convertToBaseUnit(this.value)
             + other.unit.convertToBaseUnit(other.value);
    }

    /**
     * Adds another QuantityLength to this one (UC6).
     * Result is in THIS object's unit.
     */
    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    /**
     * Adds another QuantityLength to this one with an explicit target unit (UC7).
     * Returns a NEW QuantityLength (immutable).
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseSum = addToBaseUnit(other);
        double result  = targetUnit.convertFromBaseUnit(baseSum);

        double factor = 100.0;
        result = Math.round(result * factor) / factor;

        return new QuantityLength(result, targetUnit);
    }
}
