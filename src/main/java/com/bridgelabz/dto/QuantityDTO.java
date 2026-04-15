package com.bridgelabz.dto;

public class QuantityDTO {

    // Inner interface — completely separate from the app's IMeasurable
    public interface IMeasurableUnit {
        String getMeasurementType();
        String getUnitName();
    }

    // Inner enums implementing IMeasurableUnit
    public enum LengthUnit implements IMeasurableUnit {
        INCH, FEET, YARD, CENTIMETER, METER;
        @Override public String getMeasurementType() { return "LENGTH"; }
        @Override public String getUnitName() { return this.name(); }
    }

    public enum WeightUnit implements IMeasurableUnit {
        GRAM, KILOGRAM, POUND, OUNCE, TONNE;
        @Override public String getMeasurementType() { return "WEIGHT"; }
        @Override public String getUnitName() { return this.name(); }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        MILLILITER, LITER, GALLON, CUP;
        @Override public String getMeasurementType() { return "VOLUME"; }
        @Override public String getUnitName() { return this.name(); }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;
        @Override public String getMeasurementType() { return "TEMPERATURE"; }
        @Override public String getUnitName() { return this.name(); }
    }

    public enum ResultUnit implements IMeasurableUnit {
        BOOLEAN, SCALAR;
        @Override public String getMeasurementType() { return "RESULT"; }
        @Override public String getUnitName() { return this.name(); }
    }

    // Fields
    private final double value;
    private final IMeasurableUnit unit;

    // Constructor
    public QuantityDTO(double value, IMeasurableUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    // Getters
    public double getValue() { return value; }
    public IMeasurableUnit getUnit() { return unit; }
    public String getUnitName() { return unit.getUnitName(); }
    public String getMeasurementType() { return unit.getMeasurementType(); }

    @Override
    public String toString() { return value + " " + unit.getUnitName(); }
}
