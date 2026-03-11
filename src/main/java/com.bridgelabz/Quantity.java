package com.bridgelabz;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Quantity)) return false;
        
        Quantity<?> other = (Quantity<?>) obj;
        if (this.unit.getClass() != other.unit.getClass()) return false;
        
        @SuppressWarnings("unchecked")
        Quantity<U> typedOther = (Quantity<U>) other;
        
        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = typedOther.unit.convertToBaseUnit(typedOther.value);
        
        return Double.compare(
                Math.round(base1 * 1e6) / 1e6, 
                Math.round(base2 * 1e6) / 1e6) == 0;
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(roundToTwoDecimals(result), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) throw new IllegalArgumentException("Operand or target unit cannot be null");
        double base = unit.convertToBaseUnit(value) + 
                      other.unit.convertToBaseUnit(other.value);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(roundToTwoDecimals(result), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (other.unit.getClass() != this.unit.getClass()) 
            throw new IllegalArgumentException("Cannot subtract different categories");
        if (!Double.isFinite(other.value)) throw new IllegalArgumentException("Value must be finite");
        double base = unit.convertToBaseUnit(value) - other.unit.convertToBaseUnit(other.value);
        double result = unit.convertFromBaseUnit(base);
        return new Quantity<>(roundToTwoDecimals(result), this.unit);
    }
    
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) throw new IllegalArgumentException("Operand or target unit cannot be null");
        if (other.unit.getClass() != this.unit.getClass()) 
            throw new IllegalArgumentException("Cannot subtract different categories");
        double base = unit.convertToBaseUnit(value) - other.unit.convertToBaseUnit(other.value);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(roundToTwoDecimals(result), targetUnit);
    }

    public double divide(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (other.unit.getClass() != this.unit.getClass()) 
            throw new IllegalArgumentException("Cannot divide different categories");
        if (!Double.isFinite(other.value)) throw new IllegalArgumentException("Value must be finite");
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        if (base2 == 0.0) throw new ArithmeticException("Division by zero");
        return base1 / base2;
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(unit.convertToBaseUnit(value) * 1e6) / 1e6, unit.getClass());
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}
