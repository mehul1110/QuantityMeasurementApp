package com.bridgelabz;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (b == 0.0) throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator operation;

        ArithmeticOperation(DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        public double compute(double a, double b) {
            return operation.applyAsDouble(a, b);
        }
    }

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
        if (this.unit instanceof TemperatureUnit) {
            double celsius = this.unit.convertToBaseUnit(this.value);
            double result = targetUnit.convertFromBaseUnit(celsius);
            return new Quantity<>(roundToTwoDecimals(result), targetUnit);
        }
        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(roundToTwoDecimals(result), targetUnit);
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, 
                                            boolean requireTargetUnit) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (other.unit.getClass() != this.unit.getClass()) 
            throw new IllegalArgumentException("Cannot operate on different categories");
        if (!Double.isFinite(other.value)) 
            throw new IllegalArgumentException("Value must be finite");
        if (requireTargetUnit && targetUnit == null) 
            throw new IllegalArgumentException("Target unit cannot be null");
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op) {
        this.unit.validateOperationSupport(op.name());
        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return op.compute(base1, base2);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double base = performBaseArithmetic(other, ArithmeticOperation.ADD);
        return new Quantity<>(roundToTwoDecimals(targetUnit.convertFromBaseUnit(base)), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double base = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        return new Quantity<>(roundToTwoDecimals(targetUnit.convertFromBaseUnit(base)), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
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
