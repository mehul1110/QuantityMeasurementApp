package com.bridgelabz;

public class QuantityMeasurementApp {

    public static boolean demonstrateEquality(Quantity<?> q1, Quantity<?> q2) {
        boolean result = q1.equals(q2);
        System.out.println(q1 + " == " + q2 + " ? " + result);
        return result;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> q, U targetUnit) {
        Quantity<U> converted = q.convertTo(targetUnit);
        System.out.println(q + " => " + converted);
        return converted;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = q1.add(q2, targetUnit);
        System.out.println(q1 + " + " + q2 + " = " + result);
        return result;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = q1.subtract(q2, targetUnit);
        System.out.println(q1 + " - " + q2 + " = " + result);
        return result;
    }

    public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
        double result = q1.divide(q2);
        System.out.println(q1 + " / " + q2 + " = " + result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("--- Equality checks ---");
        Quantity<LengthUnit> oneFoot      = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches = new Quantity<>(12.0, LengthUnit.INCHES);
        demonstrateEquality(oneFoot, twelveInches);

        Quantity<LengthUnit> oneYard         = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> thirtySixInches = new Quantity<>(36.0, LengthUnit.INCHES);
        demonstrateEquality(oneYard, thirtySixInches);

        System.out.println("\n--- UC5: Conversions ---");
        demonstrateConversion(new Quantity<>(1.0,  LengthUnit.FEET), LengthUnit.INCHES);
        demonstrateConversion(new Quantity<>(24.0, LengthUnit.INCHES), LengthUnit.FEET);
        demonstrateConversion(new Quantity<>(1.0,  LengthUnit.YARDS), LengthUnit.INCHES);
        demonstrateConversion(new Quantity<>(6.0,  LengthUnit.FEET), LengthUnit.YARDS);
        demonstrateConversion(new Quantity<>(2.54, LengthUnit.CENTIMETERS), LengthUnit.INCHES);
        demonstrateConversion(new Quantity<>(1.0, LengthUnit.YARDS), LengthUnit.FEET);

        System.out.println("\n--- UC6/UC7: Addition ---");
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(2.0, LengthUnit.FEET), LengthUnit.FEET);    // 3.0 ft
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET); // 2.0 ft
        demonstrateAddition(new Quantity<>(12.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCHES); // 24.0 in
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.YARDS);  // 2.0 yd
        
        System.out.println("\n--- UC7: Addition with explicit target unit ---");
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.INCHES); // 24.0 in
        demonstrateAddition(new Quantity<>(2.0, LengthUnit.YARDS), new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.FEET);     // 9.0 ft

        System.out.println("\n--- UC9: Weight ---");
        demonstrateEquality(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM));
        demonstrateConversion(new Quantity<>(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        demonstrateAddition(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM);
        System.out.println("\n--- UC11: Volume ---");
        demonstrateEquality(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        demonstrateConversion(new Quantity<>(1.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        demonstrateAddition(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE);
        System.out.println("\n--- UC12: Subtraction and Division ---");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(5.0, LengthUnit.FEET), LengthUnit.FEET);
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);
        demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM));

        System.out.println("\n--- UC14: Temperature ---");
        demonstrateEquality(new Quantity<>(100.0, TemperatureUnit.CELSIUS), new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT));
        demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT);
        System.out.println("Attempting to add temperatures:");
        try {
            demonstrateAddition(new Quantity<>(100.0, TemperatureUnit.CELSIUS), new Quantity<>(50.0, TemperatureUnit.CELSIUS), TemperatureUnit.CELSIUS);
        } catch (UnsupportedOperationException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
