package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // UC3: Feet-to-Feet equality (same value)
    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l2));
    }

    // UC3: Inches-to-Inches equality (same value)
    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityLength l1 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(l1.equals(l2));
    }

    // UC3: 1 Foot == 12 Inches (cross-unit)
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        QuantityLength oneFoot      = new QuantityLength(1.0,  LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(oneFoot.equals(twelveInches));
    }

    // UC3: 12 Inches == 1 Foot (cross-unit, reversed)
    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength oneFoot      = new QuantityLength(1.0,  LengthUnit.FEET);
        assertTrue(twelveInches.equals(oneFoot));
    }

    // UC3: Different feet values → false
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(l1.equals(l2));
    }

    // UC3: Different inch values → false
    @Test
    void testEquality_InchToInch_DifferentValue() {
        QuantityLength l1 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(24.0, LengthUnit.INCHES);
        assertFalse(l1.equals(l2));
    }

    // UC3: Null comparison → false
    @Test
    void testEquality_NullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(l1.equals(null));
    }

    // UC3: Same reference → true
    @Test
    void testEquality_SameReference() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l1));
    }

    // UC3: Different type → false
    @Test
    void testEquality_DifferentType() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(l1.equals("string"));
    }

    // ─── UC4: YARD TESTS ────────────────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength l2 = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength l2 = new QuantityLength(2.0, LengthUnit.YARDS);
        assertFalse(l1.equals(l2));
    }

    @Test
    void testEquality_YardToFeet_Equivalent() {
        QuantityLength oneYard   = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength threeFeet = new QuantityLength(3.0, LengthUnit.FEET);
        assertTrue(oneYard.equals(threeFeet));
    }

    @Test
    void testEquality_FeetToYard_Equivalent() {
        QuantityLength threeFeet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength oneYard   = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(threeFeet.equals(oneYard));
    }

    @Test
    void testEquality_YardToInches_Equivalent() {
        QuantityLength oneYard        = new QuantityLength(1.0,  LengthUnit.YARDS);
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(oneYard.equals(thirtySixInches));
    }

    @Test
    void testEquality_InchesToYard_Equivalent() {
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength oneYard         = new QuantityLength(1.0,  LengthUnit.YARDS);
        assertTrue(thirtySixInches.equals(oneYard));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalent() {
        QuantityLength oneYard  = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength twoFeet  = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(oneYard.equals(twoFeet));
    }

    @Test
    void testEquality_YardNullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.YARDS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: CENTIMETER TESTS ──────────────────────────────────────────────────

    @Test
    void testEquality_CmToCm_SameValue() {
        QuantityLength l1 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        QuantityLength l2 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        assertTrue(l1.equals(l2));
    }

    // 2.54 cm ≈ 1 inch — compare converted-to-feet values using delta 1e-4
    @Test
    void testEquality_CmToInches_Equivalent() {
        double cmFeet   = 2.54  * LengthUnit.CENTIMETERS.getConversionFactor();
        double inchFeet = 1.0   * LengthUnit.INCHES.getConversionFactor();
        assertEquals(cmFeet, inchFeet, 1e-4);
    }

    @Test
    void testEquality_CmToFeet_NonEquivalent() {
        QuantityLength oneCm   = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(oneCm.equals(oneFoot));
    }

    @Test
    void testEquality_CmNullComparison() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_CmSameReference() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: TRANSITIVE PROPERTY TEST ──────────────────────────────────────────

    // 1 yard == 3 feet, 3 feet == 36 inches  =>  1 yard == 36 inches
    @Test
    void testEquality_TransitiveProperty() {
        QuantityLength oneYard         = new QuantityLength(1.0,  LengthUnit.YARDS);
        QuantityLength threeFeet       = new QuantityLength(3.0,  LengthUnit.FEET);
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);

        assertTrue(oneYard.equals(threeFeet));
        assertTrue(threeFeet.equals(thirtySixInches));
        assertTrue(oneYard.equals(thirtySixInches));
    }

    // ─── UC5: CONVERSION TESTS ──────────────────────────────────────────────────

    @Test
    void testConversion_FeetToInches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToFeet() {
        QuantityLength result = new QuantityLength(24.0, LengthUnit.INCHES)
                .convertTo(LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_YardsToInches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.YARDS)
                .convertTo(LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToYards() {
        QuantityLength result = new QuantityLength(72.0, LengthUnit.INCHES)
                .convertTo(LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_FeetToYards() {
        QuantityLength result = new QuantityLength(6.0, LengthUnit.FEET)
                .convertTo(LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_CentimetersToInches() {
        QuantityLength result = new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                .convertTo(LengthUnit.INCHES);
        assertEquals(1.0, result.getValue(), 1e-4);
    }

    @Test
    void testConversion_SameUnit() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .convertTo(LengthUnit.FEET);
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_ZeroValue() {
        QuantityLength result = new QuantityLength(0.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_NegativeValue() {
        QuantityLength result = new QuantityLength(-1.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(-12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_RoundTrip() {
        double original = 5.0;
        QuantityLength inFeet = new QuantityLength(original, LengthUnit.FEET);
        QuantityLength inInches = inFeet.convertTo(LengthUnit.INCHES);
        QuantityLength backToFeet = inInches.convertTo(LengthUnit.FEET);
        assertEquals(original, backToFeet.getValue(), 1e-6);
    }

    @Test
    void testConversion_NullTargetUnit_ThrowsException() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> l1.convertTo(null));
    }

    @Test
    void testConversion_NaNValue_ThrowsException() {
        QuantityLength l1 = new QuantityLength(Double.NaN, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,
                () -> l1.convertTo(LengthUnit.INCHES));
    }

    @Test
    void testConversion_InfiniteValue_ThrowsException() {
        QuantityLength l1 = new QuantityLength(Double.POSITIVE_INFINITY, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,
                () -> l1.convertTo(LengthUnit.INCHES));
    }

    // ─── UC6: ADDITION TESTS ────────────────────────────────────────────────────

    // 1.0 ft + 2.0 ft = 3.0 ft
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(2.0, LengthUnit.FEET));
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 6.0 in + 6.0 in = 12.0 in
    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityLength result = new QuantityLength(6.0, LengthUnit.INCHES)
                .add(new QuantityLength(6.0, LengthUnit.INCHES));
        assertEquals(12.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0 ft + 12.0 in = 2.0 ft  (result in first operand's unit: FEET)
    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 12.0 in + 1.0 ft = 24.0 in  (result in first operand's unit: INCHES)
    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength result = new QuantityLength(12.0, LengthUnit.INCHES)
                .add(new QuantityLength(1.0, LengthUnit.FEET));
        assertEquals(24.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0 yd + 3.0 ft = 2.0 yd
    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.54 cm + 1.0 in ≈ 5.08 cm  (delta 1e-2)
    @Test
    void testAddition_CrossUnit_CmPlusInch() {
        QuantityLength result = new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                .add(new QuantityLength(1.0, LengthUnit.INCHES));
        assertEquals(5.08, result.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
    }

    // 5.0 ft + 0.0 in = 5.0 ft
    @Test
    void testAddition_WithZero() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(0.0, LengthUnit.INCHES));
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    // 5.0 ft + (-2.0 ft) = 3.0 ft
    @Test
    void testAddition_NegativeValues() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(-2.0, LengthUnit.FEET));
        assertEquals(3.0, result.getValue(), 1e-6);
    }

    // Commutativity: base value of (1ft + 12in) == base value of (12in + 1ft)
    @Test
    void testAddition_Commutativity() {
        QuantityLength ab = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES)); // in FEET
        QuantityLength ba = new QuantityLength(12.0, LengthUnit.INCHES)
                .add(new QuantityLength(1.0, LengthUnit.FEET));    // in INCHES

        double abBase = ab.getValue() * ab.getUnit().getConversionFactor();
        double baBase = ba.getValue() * ba.getUnit().getConversionFactor();
        assertEquals(abBase, baBase, 1e-6);
    }

    // add(null) → IllegalArgumentException
    @Test
    void testAddition_NullOperand_ThrowsException() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> l1.add(null));
    }

    // 1e6 ft + 1e6 ft = 2e6 ft
    @Test
    void testAddition_LargeValues() {
        QuantityLength result = new QuantityLength(1e6, LengthUnit.FEET)
                .add(new QuantityLength(1e6, LengthUnit.FEET));
        assertEquals(2e6, result.getValue(), 1e-6);
    }

    // ─── UC7: ADDITION WITH EXPLICIT TARGET UNIT ─────────────────────────────────

    // 1.0ft + 12.0in expressed in FEET = 2.0 ft
    @Test
    void testAddition_ExplicitTarget_Feet() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES),
                        LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 1.0ft + 12.0in expressed in INCHES = 24.0 in
    @Test
    void testAddition_ExplicitTarget_Inches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES),
                        LengthUnit.INCHES);
        assertEquals(24.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0ft + 12.0in expressed in YARDS ≈ 0.67 yd
    @Test
    void testAddition_ExplicitTarget_Yards() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        assertEquals(0.67, result.getValue(), 1e-2);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.0yd + 3.0ft expressed in YARDS = 3.0 yd
    @Test
    void testAddition_ExplicitTarget_SameAsFirst() {
        QuantityLength result = new QuantityLength(2.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET),
                        LengthUnit.YARDS);
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.0yd + 3.0ft expressed in FEET = 9.0 ft
    @Test
    void testAddition_ExplicitTarget_SameAsSecond() {
        QuantityLength result = new QuantityLength(2.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET),
                        LengthUnit.FEET);
        assertEquals(9.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // Commutativity: base of (1ft +[YARDS] 12in) == base of (12in +[YARDS] 1ft)
    @Test
    void testAddition_ExplicitTarget_Commutativity() {
        QuantityLength ab = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        QuantityLength ba = new QuantityLength(12.0, LengthUnit.INCHES)
                .add(new QuantityLength(1.0, LengthUnit.FEET),
                        LengthUnit.YARDS);
        assertEquals(ab.getValue() * ab.getUnit().getConversionFactor(),
                     ba.getValue() * ba.getUnit().getConversionFactor(), 1e-6);
    }

    // 5.0ft + 0.0in in YARDS ≈ 1.667 yd
    @Test
    void testAddition_ExplicitTarget_WithZero() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(0.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        assertEquals(5.0 / 3.0, result.getValue(), 1e-2);
    }

    // 5.0ft + (-2.0ft) in INCHES = 36.0 in
    @Test
    void testAddition_ExplicitTarget_NegativeValues() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(-2.0, LengthUnit.FEET),
                        LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // null targetUnit → IllegalArgumentException
    @Test
    void testAddition_ExplicitTarget_NullTargetUnit_ThrowsException() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);
        assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
    }

    // 1000ft + 500ft expressed in INCHES = 18000.0 in
    @Test
    void testAddition_ExplicitTarget_LargeToSmall() {
        QuantityLength result = new QuantityLength(1000.0, LengthUnit.FEET)
                .add(new QuantityLength(500.0, LengthUnit.FEET),
                        LengthUnit.INCHES);
        assertEquals(18000.0, result.getValue(), 1e-6);
    }

    // ─── UC8: STANDALONE LengthUnit TESTS ───────────────────────────────────────

    @Test
    void testLengthUnit_FeetConversionFactor() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), 1e-9);
    }

    @Test
    void testLengthUnit_InchesConversionFactor() {
        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), 1e-4);
    }

    @Test
    void testLengthUnit_YardsConversionFactor() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), 1e-9);
    }

    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), 1e-9);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), 1e-9);
    }

    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), 1e-9);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), 1e-9);
    }
}


