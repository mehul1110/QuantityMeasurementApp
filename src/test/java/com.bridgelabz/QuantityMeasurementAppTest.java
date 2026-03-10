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

    // ─── UC4: YARD TESTS ────────────────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        QuantityLength l2 = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        QuantityLength l2 = new QuantityLength(2.0, QuantityLength.LengthUnit.YARDS);
        assertFalse(l1.equals(l2));
    }

    @Test
    void testEquality_YardToFeet_Equivalent() {
        QuantityLength oneYard   = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        QuantityLength threeFeet = new QuantityLength(3.0, QuantityLength.LengthUnit.FEET);
        assertTrue(oneYard.equals(threeFeet));
    }

    @Test
    void testEquality_FeetToYard_Equivalent() {
        QuantityLength threeFeet = new QuantityLength(3.0, QuantityLength.LengthUnit.FEET);
        QuantityLength oneYard   = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        assertTrue(threeFeet.equals(oneYard));
    }

    @Test
    void testEquality_YardToInches_Equivalent() {
        QuantityLength oneYard        = new QuantityLength(1.0,  QuantityLength.LengthUnit.YARDS);
        QuantityLength thirtySixInches = new QuantityLength(36.0, QuantityLength.LengthUnit.INCHES);
        assertTrue(oneYard.equals(thirtySixInches));
    }

    @Test
    void testEquality_InchesToYard_Equivalent() {
        QuantityLength thirtySixInches = new QuantityLength(36.0, QuantityLength.LengthUnit.INCHES);
        QuantityLength oneYard         = new QuantityLength(1.0,  QuantityLength.LengthUnit.YARDS);
        assertTrue(thirtySixInches.equals(oneYard));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalent() {
        QuantityLength oneYard  = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        QuantityLength twoFeet  = new QuantityLength(2.0, QuantityLength.LengthUnit.FEET);
        assertFalse(oneYard.equals(twoFeet));
    }

    @Test
    void testEquality_YardNullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: CENTIMETER TESTS ──────────────────────────────────────────────────

    @Test
    void testEquality_CmToCm_SameValue() {
        QuantityLength l1 = new QuantityLength(2.0, QuantityLength.LengthUnit.CENTIMETERS);
        QuantityLength l2 = new QuantityLength(2.0, QuantityLength.LengthUnit.CENTIMETERS);
        assertTrue(l1.equals(l2));
    }

    // 2.54 cm ≈ 1 inch — compare converted-to-feet values using delta 1e-4
    @Test
    void testEquality_CmToInches_Equivalent() {
        double cmFeet   = 2.54  * QuantityLength.LengthUnit.CENTIMETERS.getConversionFactor();
        double inchFeet = 1.0   * QuantityLength.LengthUnit.INCHES.getConversionFactor();
        assertEquals(cmFeet, inchFeet, 1e-4);
    }

    @Test
    void testEquality_CmToFeet_NonEquivalent() {
        QuantityLength oneCm   = new QuantityLength(1.0, QuantityLength.LengthUnit.CENTIMETERS);
        QuantityLength oneFoot = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertFalse(oneCm.equals(oneFoot));
    }

    @Test
    void testEquality_CmNullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.CENTIMETERS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_CmSameReference() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.CENTIMETERS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: TRANSITIVE PROPERTY TEST ──────────────────────────────────────────

    // 1 yard == 3 feet, 3 feet == 36 inches  =>  1 yard == 36 inches
    @Test
    void testEquality_TransitiveProperty() {
        QuantityLength oneYard         = new QuantityLength(1.0,  QuantityLength.LengthUnit.YARDS);
        QuantityLength threeFeet       = new QuantityLength(3.0,  QuantityLength.LengthUnit.FEET);
        QuantityLength thirtySixInches = new QuantityLength(36.0, QuantityLength.LengthUnit.INCHES);

        assertTrue(oneYard.equals(threeFeet));
        assertTrue(threeFeet.equals(thirtySixInches));
        assertTrue(oneYard.equals(thirtySixInches));
    }

    // ─── UC5: CONVERSION TESTS ──────────────────────────────────────────────────

    @Test
    void testConversion_FeetToInches() {
        QuantityLength result = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET)
                .convertTo(QuantityLength.LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToFeet() {
        QuantityLength result = new QuantityLength(24.0, QuantityLength.LengthUnit.INCHES)
                .convertTo(QuantityLength.LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_YardsToInches() {
        QuantityLength result = new QuantityLength(1.0, QuantityLength.LengthUnit.YARDS)
                .convertTo(QuantityLength.LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToYards() {
        QuantityLength result = new QuantityLength(72.0, QuantityLength.LengthUnit.INCHES)
                .convertTo(QuantityLength.LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_FeetToYards() {
        QuantityLength result = new QuantityLength(6.0, QuantityLength.LengthUnit.FEET)
                .convertTo(QuantityLength.LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_CentimetersToInches() {
        QuantityLength result = new QuantityLength(2.54, QuantityLength.LengthUnit.CENTIMETERS)
                .convertTo(QuantityLength.LengthUnit.INCHES);
        assertEquals(1.0, result.getValue(), 1e-4);
    }

    @Test
    void testConversion_SameUnit() {
        QuantityLength result = new QuantityLength(5.0, QuantityLength.LengthUnit.FEET)
                .convertTo(QuantityLength.LengthUnit.FEET);
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_ZeroValue() {
        QuantityLength result = new QuantityLength(0.0, QuantityLength.LengthUnit.FEET)
                .convertTo(QuantityLength.LengthUnit.INCHES);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_NegativeValue() {
        QuantityLength result = new QuantityLength(-1.0, QuantityLength.LengthUnit.FEET)
                .convertTo(QuantityLength.LengthUnit.INCHES);
        assertEquals(-12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_RoundTrip() {
        double original = 5.0;
        QuantityLength inFeet = new QuantityLength(original, QuantityLength.LengthUnit.FEET);
        QuantityLength inInches = inFeet.convertTo(QuantityLength.LengthUnit.INCHES);
        QuantityLength backToFeet = inInches.convertTo(QuantityLength.LengthUnit.FEET);
        assertEquals(original, backToFeet.getValue(), 1e-6);
    }

    @Test
    void testConversion_NullTargetUnit_ThrowsException() {
        QuantityLength l1 = new QuantityLength(1.0, QuantityLength.LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> l1.convertTo(null));
    }

    @Test
    void testConversion_NaNValue_ThrowsException() {
        QuantityLength l1 = new QuantityLength(Double.NaN, QuantityLength.LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,
                () -> l1.convertTo(QuantityLength.LengthUnit.INCHES));
    }

    @Test
    void testConversion_InfiniteValue_ThrowsException() {
        QuantityLength l1 = new QuantityLength(Double.POSITIVE_INFINITY, QuantityLength.LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,
                () -> l1.convertTo(QuantityLength.LengthUnit.INCHES));
    }
}
