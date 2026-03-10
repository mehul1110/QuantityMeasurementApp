package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // UC3: Feet-to-Feet equality (same value)
    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertTrue(l1.equals(l2));
    }

    // UC3: Inches-to-Inches equality (same value)
    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityLength l1 = new QuantityLength(12.0, QuantityLength.LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(12.0, QuantityLength.LengthUnit.INCHES);
        assertTrue(l1.equals(l2));
    }

    // UC3: 1 Foot == 12 Inches (cross-unit)
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        QuantityLength oneFoot      = new QuantityLength(1.0,  QuantityLength.LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, QuantityLength.LengthUnit.INCHES);
        assertTrue(oneFoot.equals(twelveInches));
    }

    // UC3: 12 Inches == 1 Foot (cross-unit, reversed)
    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        QuantityLength twelveInches = new QuantityLength(12.0, QuantityLength.LengthUnit.INCHES);
        QuantityLength oneFoot      = new QuantityLength(1.0,  QuantityLength.LengthUnit.FEET);
        assertTrue(twelveInches.equals(oneFoot));
    }

    // UC3: Different feet values → false
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(2.0, QuantityLength.LengthUnit.FEET);
        assertFalse(l1.equals(l2));
    }

    // UC3: Different inch values → false
    @Test
    void testEquality_InchToInch_DifferentValue() {
        QuantityLength l1 = new QuantityLength(12.0, QuantityLength.LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(24.0, QuantityLength.LengthUnit.INCHES);
        assertFalse(l1.equals(l2));
    }

    // UC3: Null comparison → false
    @Test
    void testEquality_NullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertFalse(l1.equals(null));
    }

    // UC3: Same reference → true
    @Test
    void testEquality_SameReference() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertTrue(l1.equals(l1));
    }

    // UC3: Different type → false
    @Test
    void testEquality_DifferentType() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertFalse(l1.equals("string"));
    }
}