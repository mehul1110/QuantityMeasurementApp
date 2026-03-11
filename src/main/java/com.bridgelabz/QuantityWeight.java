package com.bridgelabz;

/**
 * Immutable value object representing a weight measurement.
 * All unit-conversion math is fully delegated to WeightUnit.
 * Weight and Length are incompatible — equals() enforces strict class-level type check.
 */
public class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;   // strict: no cross-category
        QuantityWeight other = (QuantityWeight) obj;
        double thisKg  = this.unit.convertToBaseUnit(this.value);
        double otherKg = other.unit.convertToBaseUnit(other.value);
        return Double.compare(thisKg, otherKg) == 0;
    }

    @Override
    public String toString() {
        return value + " " + unit.name();
    }

    /**
     * Converts this QuantityWeight to the target unit.
     * Returns a NEW QuantityWeight object (immutable).
     */
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        if (Double.isNaN(this.value) || Double.isInfinite(this.value))
            throw new IllegalArgumentException("Value must be finite");

        double base   = this.unit.convertToBaseUnit(this.value);
        double result = targetUnit.convertFromBaseUnit(base);

        double factor = 1_000_000.0;
        result = Math.round(result * factor) / factor;

        return new QuantityWeight(result, targetUnit);
    }

    /** Helper: converts both operands to kg (base unit) and returns their sum. */
    private double addToBaseUnit(QuantityWeight other) {
        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");
        if (Double.isNaN(other.value) || Double.isInfinite(other.value))
            throw new IllegalArgumentException("Operand value must be finite");

        return this.unit.convertToBaseUnit(this.value)
             + other.unit.convertToBaseUnit(other.value);
    }

    /**
     * Adds another QuantityWeight to this one.
     * Result is in THIS object's unit (delegates to add(other, this.unit)).
     */
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    /**
     * Adds another QuantityWeight to this one with an explicit target unit.
     * Returns a NEW QuantityWeight (immutable), rounded to 2 decimal places.
     */
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseSum = addToBaseUnit(other);
        double result  = targetUnit.convertFromBaseUnit(baseSum);

        double factor = 100.0;
        result = Math.round(result * factor) / factor;

        return new QuantityWeight(result, targetUnit);
    }
}
