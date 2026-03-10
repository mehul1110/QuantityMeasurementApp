package com.bridgelabz;

public class QuantityMeasurementApp {

    // Inner class representing Feet measurement
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }
    }

    // Inner class representing Inches measurement
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches inches = (Inches) obj;
            return Double.compare(inches.value, value) == 0;
        }
    }

    public static boolean compareFeet(double val1, double val2) {
        Feet feet1 = new Feet(val1);
        Feet feet2 = new Feet(val2);
        return feet1.equals(feet2);
    }

    public static boolean compareInches(double val1, double val2) {
        Inches inches1 = new Inches(val1);
        Inches inches2 = new Inches(val2);
        return inches1.equals(inches2);
    }

    public static void main(String[] args) {
        System.out.println("Comparing 1.0 Feet with 1.0 Feet: " + compareFeet(1.0, 1.0));
        System.out.println("Comparing 1.0 Feet with 2.0 Feet: " + compareFeet(1.0, 2.0));
        System.out.println("Comparing 12.0 Inches with 12.0 Inches: " + compareInches(12.0, 12.0));
        System.out.println("Comparing 12.0 Inches with 24.0 Inches: " + compareInches(12.0, 24.0));
    }
}