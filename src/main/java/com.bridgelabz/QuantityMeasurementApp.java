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

    public static void main(String[] args) {
        System.out.println("--- Same-unit comparisons ---");
        System.out.println("compareFeet(1.0, 1.0)    : " + compareFeet(1.0, 1.0));
        System.out.println("compareInches(12.0, 12.0): " + compareInches(12.0, 12.0));

        System.out.println("\n--- Cross-unit: Feet <-> Inches ---");
        QuantityLength oneFoot      = new QuantityLength(1.0,  LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        demonstrateLengthEquality(oneFoot, twelveInches);   // true

        System.out.println("\n--- Cross-unit: Yards ---");
        QuantityLength oneYard   = new QuantityLength(1.0,  LengthUnit.YARDS);
        QuantityLength threeFeet = new QuantityLength(3.0,  LengthUnit.FEET);
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        demonstrateLengthEquality(oneYard, threeFeet);         // true
        demonstrateLengthEquality(oneYard, thirtySixInches);   // true

        System.out.println("\n--- Cross-unit: Centimeters ---");
        QuantityLength twoPointFiveFourCm = new QuantityLength(2.54,  LengthUnit.CENTIMETERS);
        QuantityLength oneInch            = new QuantityLength(1.0,   LengthUnit.INCHES);
        demonstrateLengthEquality(twoPointFiveFourCm, oneInch);  // approximately true
    }
}