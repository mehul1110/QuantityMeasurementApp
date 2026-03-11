package com.bridgelabz;

import com.bridgelabz.QuantityLength.LengthUnit;

public class QuantityMeasurementApp {

    public static boolean compareFeet(double val1, double val2) {
        QuantityLength l1 = new QuantityLength(val1, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(val2, LengthUnit.FEET);
        return l1.equals(l2);
    }

    public static boolean compareInches(double val1, double val2) {
        QuantityLength l1 = new QuantityLength(val1, LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(val2, LengthUnit.INCHES);
        return l1.equals(l2);
    }

    public static boolean demonstrateLengthEquality(QuantityLength l1, QuantityLength l2) {
        boolean result = l1.equals(l2);
        System.out.println(l1 + " == " + l2 + " ? " + result);
        return result;
    }

    public static QuantityLength demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        QuantityLength source = new QuantityLength(value, from);
        QuantityLength converted = source.convertTo(to);
        System.out.println(source + " => " + converted);
        return converted;
    }

    public static QuantityLength demonstrateLengthConversion(QuantityLength length, LengthUnit to) {
        QuantityLength converted = length.convertTo(to);
        System.out.println(length + " => " + converted);
        return converted;
    }

    public static QuantityLength demonstrateLengthAddition(QuantityLength l1, QuantityLength l2) {
        QuantityLength result = l1.add(l2);
        System.out.println(l1 + " + " + l2 + " = " + result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("--- Equality checks ---");
        QuantityLength oneFoot      = new QuantityLength(1.0,  LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        demonstrateLengthEquality(oneFoot, twelveInches);

        QuantityLength oneYard         = new QuantityLength(1.0,  LengthUnit.YARDS);
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        demonstrateLengthEquality(oneYard, thirtySixInches);

        System.out.println("\n--- UC5: Conversions ---");
        demonstrateLengthConversion(1.0,  LengthUnit.FEET,        LengthUnit.INCHES);
        demonstrateLengthConversion(24.0, LengthUnit.INCHES,      LengthUnit.FEET);
        demonstrateLengthConversion(1.0,  LengthUnit.YARDS,       LengthUnit.INCHES);
        demonstrateLengthConversion(6.0,  LengthUnit.FEET,        LengthUnit.YARDS);
        demonstrateLengthConversion(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(new QuantityLength(1.0, LengthUnit.YARDS), LengthUnit.FEET);

        System.out.println("\n--- UC6: Addition ---");
        demonstrateLengthAddition(new QuantityLength(1.0,  LengthUnit.FEET),
                                  new QuantityLength(2.0,  LengthUnit.FEET));     // 3.0 ft
        demonstrateLengthAddition(new QuantityLength(1.0,  LengthUnit.FEET),
                                  new QuantityLength(12.0, LengthUnit.INCHES));   // 2.0 ft
        demonstrateLengthAddition(new QuantityLength(12.0, LengthUnit.INCHES),
                                  new QuantityLength(1.0,  LengthUnit.FEET));     // 24.0 in
        demonstrateLengthAddition(new QuantityLength(1.0,  LengthUnit.YARDS),
                                  new QuantityLength(3.0,  LengthUnit.FEET));     // 2.0 yd

        System.out.println("\n--- UC7: Addition with explicit target unit ---");
        QuantityLength sum1 = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        System.out.println("1.0 FEET + 12.0 INCHES -> " + sum1);     // 24.0 INCHES
        QuantityLength sum2 = new QuantityLength(2.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.FEET);
        System.out.println("2.0 YARDS + 3.0 FEET -> " + sum2);       // 9.0 FEET
    }
}
