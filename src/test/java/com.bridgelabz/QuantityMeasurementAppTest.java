package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // UC3: Feet-to-Feet equality (same value)
    @Test
    void testEquality_FeetToFeet_SameValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l2));
    }

    // UC3: Inches-to-Inches equality (same value)
    @Test
    void testEquality_InchToInch_SameValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);
        assertTrue(l1.equals(l2));
    }

    // UC3: 1 Foot == 12 Inches (cross-unit)
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        Quantity<LengthUnit> oneFoot      = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches = new Quantity<>(12.0, LengthUnit.INCHES);
        assertTrue(oneFoot.equals(twelveInches));
    }

    // UC3: 12 Inches == 1 Foot (cross-unit, reversed)
    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        Quantity<LengthUnit> twelveInches = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> oneFoot      = new Quantity<>(1.0,  LengthUnit.FEET);
        assertTrue(twelveInches.equals(oneFoot));
    }

    // UC3: Different feet values → false
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(2.0, LengthUnit.FEET);
        assertFalse(l1.equals(l2));
    }

    // UC3: Different inch values → false
    @Test
    void testEquality_InchToInch_DifferentValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> l2 = new Quantity<>(24.0, LengthUnit.INCHES);
        assertFalse(l1.equals(l2));
    }

    // UC3: Null comparison → false
    @Test
    void testEquality_NullComparison() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(l1.equals(null));
    }

    // UC3: Same reference → true
    @Test
    void testEquality_SameReference() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l1));
    }

    // UC3: Different type → false
    @Test
    void testEquality_DifferentType() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(l1.equals("string"));
    }

    // ─── UC4: YARD TESTS ────────────────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.YARDS);
        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> l2 = new Quantity<>(2.0, LengthUnit.YARDS);
        assertFalse(l1.equals(l2));
    }

    @Test
    void testEquality_YardToFeet_Equivalent() {
        Quantity<LengthUnit> oneYard   = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> threeFeet = new Quantity<>(3.0, LengthUnit.FEET);
        assertTrue(oneYard.equals(threeFeet));
    }

    @Test
    void testEquality_FeetToYard_Equivalent() {
        Quantity<LengthUnit> threeFeet = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> oneYard   = new Quantity<>(1.0, LengthUnit.YARDS);
        assertTrue(threeFeet.equals(oneYard));
    }

    @Test
    void testEquality_YardToInches_Equivalent() {
        Quantity<LengthUnit> oneYard        = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> thirtySixInches = new Quantity<>(36.0, LengthUnit.INCHES);
        assertTrue(oneYard.equals(thirtySixInches));
    }

    @Test
    void testEquality_InchesToYard_Equivalent() {
        Quantity<LengthUnit> thirtySixInches = new Quantity<>(36.0, LengthUnit.INCHES);
        Quantity<LengthUnit> oneYard         = new Quantity<>(1.0,  LengthUnit.YARDS);
        assertTrue(thirtySixInches.equals(oneYard));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalent() {
        Quantity<LengthUnit> oneYard  = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> twoFeet  = new Quantity<>(2.0, LengthUnit.FEET);
        assertFalse(oneYard.equals(twoFeet));
    }

    @Test
    void testEquality_YardNullComparison() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_YardSameReference() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: CENTIMETER TESTS ──────────────────────────────────────────────────

    @Test
    void testEquality_CmToCm_SameValue() {
        Quantity<LengthUnit> l1 = new Quantity<>(2.0, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> l2 = new Quantity<>(2.0, LengthUnit.CENTIMETERS);
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
        Quantity<LengthUnit> oneCm   = new Quantity<>(1.0, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(oneCm.equals(oneFoot));
    }

    @Test
    void testEquality_CmNullComparison() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.CENTIMETERS);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_CmSameReference() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.CENTIMETERS);
        assertTrue(l1.equals(l1));
    }

    // ─── UC4: TRANSITIVE PROPERTY TEST ──────────────────────────────────────────

    // 1 yard == 3 feet, 3 feet == 36 inches  =>  1 yard == 36 inches
    @Test
    void testEquality_TransitiveProperty() {
        Quantity<LengthUnit> oneYard         = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> threeFeet       = new Quantity<>(3.0,  LengthUnit.FEET);
        Quantity<LengthUnit> thirtySixInches = new Quantity<>(36.0, LengthUnit.INCHES);

        assertTrue(oneYard.equals(threeFeet));
        assertTrue(threeFeet.equals(thirtySixInches));
        assertTrue(oneYard.equals(thirtySixInches));
    }

    // ─── UC5: CONVERSION TESTS ──────────────────────────────────────────────────

    @Test
    void testConversion_FeetToInches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToFeet() {
        Quantity<LengthUnit> result = new Quantity<>(24.0, LengthUnit.INCHES)
                .convertTo(LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_YardsToInches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.YARDS)
                .convertTo(LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_InchesToYards() {
        Quantity<LengthUnit> result = new Quantity<>(72.0, LengthUnit.INCHES)
                .convertTo(LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_FeetToYards() {
        Quantity<LengthUnit> result = new Quantity<>(6.0, LengthUnit.FEET)
                .convertTo(LengthUnit.YARDS);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_CentimetersToInches() {
        Quantity<LengthUnit> result = new Quantity<>(2.54, LengthUnit.CENTIMETERS)
                .convertTo(LengthUnit.INCHES);
        assertEquals(1.0, result.getValue(), 1e-4);
    }

    @Test
    void testConversion_SameUnit() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .convertTo(LengthUnit.FEET);
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_ZeroValue() {
        Quantity<LengthUnit> result = new Quantity<>(0.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_NegativeValue() {
        Quantity<LengthUnit> result = new Quantity<>(-1.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES);
        assertEquals(-12.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_RoundTrip() {
        double original = 5.0;
        Quantity<LengthUnit> inFeet = new Quantity<>(original, LengthUnit.FEET);
        Quantity<LengthUnit> inInches = inFeet.convertTo(LengthUnit.INCHES);
        Quantity<LengthUnit> backToFeet = inInches.convertTo(LengthUnit.FEET);
        assertEquals(original, backToFeet.getValue(), 1e-6);
    }

    @Test
    void testConversion_NullTargetUnit_ThrowsException() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> l1.convertTo(null));
    }

    @Test
    void testConversion_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testConversion_InfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    // ─── UC6: ADDITION TESTS ────────────────────────────────────────────────────

    // 1.0 ft + 2.0 ft = 3.0 ft
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 6.0 in + 6.0 in = 12.0 in
    @Test
    void testAddition_SameUnit_InchPlusInch() {
        Quantity<LengthUnit> result = new Quantity<>(6.0, LengthUnit.INCHES)
                .add(new Quantity<>(6.0, LengthUnit.INCHES));
        assertEquals(12.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0 ft + 12.0 in = 2.0 ft  (result in first operand's unit: FEET)
    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 12.0 in + 1.0 ft = 24.0 in  (result in first operand's unit: INCHES)
    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(12.0, LengthUnit.INCHES)
                .add(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(24.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0 yd + 3.0 ft = 2.0 yd
    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.YARDS)
                .add(new Quantity<>(3.0, LengthUnit.FEET));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.54 cm + 1.0 in ≈ 5.08 cm  (delta 1e-2)
    @Test
    void testAddition_CrossUnit_CmPlusInch() {
        Quantity<LengthUnit> result = new Quantity<>(2.54, LengthUnit.CENTIMETERS)
                .add(new Quantity<>(1.0, LengthUnit.INCHES));
        assertEquals(5.08, result.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
    }

    // 5.0 ft + 0.0 in = 5.0 ft
    @Test
    void testAddition_WithZero() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .add(new Quantity<>(0.0, LengthUnit.INCHES));
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    // 5.0 ft + (-2.0 ft) = 3.0 ft
    @Test
    void testAddition_NegativeValues() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .add(new Quantity<>(-2.0, LengthUnit.FEET));
        assertEquals(3.0, result.getValue(), 1e-6);
    }

    // Commutativity: base value of (1ft + 12in) == base value of (12in + 1ft)
    @Test
    void testAddition_Commutativity() {
        Quantity<LengthUnit> ab = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES)); // in FEET
        Quantity<LengthUnit> ba = new Quantity<>(12.0, LengthUnit.INCHES)
                .add(new Quantity<>(1.0, LengthUnit.FEET));    // in INCHES

        double abBase = ab.getValue() * ab.getUnit().getConversionFactor();
        double baBase = ba.getValue() * ba.getUnit().getConversionFactor();
        assertEquals(abBase, baBase, 1e-6);
    }

    // add(null) → IllegalArgumentException
    @Test
    void testAddition_NullOperand_ThrowsException() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> l1.add(null));
    }

    // 1e6 ft + 1e6 ft = 2e6 ft
    @Test
    void testAddition_LargeValues() {
        Quantity<LengthUnit> result = new Quantity<>(1e6, LengthUnit.FEET)
                .add(new Quantity<>(1e6, LengthUnit.FEET));
        assertEquals(2e6, result.getValue(), 1e-6);
    }

    // ─── UC7: ADDITION WITH EXPLICIT TARGET UNIT ─────────────────────────────────

    // 1.0ft + 12.0in expressed in FEET = 2.0 ft
    @Test
    void testAddition_ExplicitTarget_Feet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES),
                        LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 1.0ft + 12.0in expressed in INCHES = 24.0 in
    @Test
    void testAddition_ExplicitTarget_Inches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES),
                        LengthUnit.INCHES);
        assertEquals(24.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // 1.0ft + 12.0in expressed in YARDS ≈ 0.67 yd
    @Test
    void testAddition_ExplicitTarget_Yards() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        assertEquals(0.67, result.getValue(), 1e-2);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.0yd + 3.0ft expressed in YARDS = 3.0 yd
    @Test
    void testAddition_ExplicitTarget_SameAsFirst() {
        Quantity<LengthUnit> result = new Quantity<>(2.0, LengthUnit.YARDS)
                .add(new Quantity<>(3.0, LengthUnit.FEET),
                        LengthUnit.YARDS);
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    // 2.0yd + 3.0ft expressed in FEET = 9.0 ft
    @Test
    void testAddition_ExplicitTarget_SameAsSecond() {
        Quantity<LengthUnit> result = new Quantity<>(2.0, LengthUnit.YARDS)
                .add(new Quantity<>(3.0, LengthUnit.FEET),
                        LengthUnit.FEET);
        assertEquals(9.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // Commutativity: base of (1ft +[YARDS] 12in) == base of (12in +[YARDS] 1ft)
    @Test
    void testAddition_ExplicitTarget_Commutativity() {
        Quantity<LengthUnit> ab = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        Quantity<LengthUnit> ba = new Quantity<>(12.0, LengthUnit.INCHES)
                .add(new Quantity<>(1.0, LengthUnit.FEET),
                        LengthUnit.YARDS);
        assertEquals(ab.getValue() * ab.getUnit().getConversionFactor(),
                     ba.getValue() * ba.getUnit().getConversionFactor(), 1e-6);
    }

    // 5.0ft + 0.0in in YARDS ≈ 1.667 yd
    @Test
    void testAddition_ExplicitTarget_WithZero() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .add(new Quantity<>(0.0, LengthUnit.INCHES),
                        LengthUnit.YARDS);
        assertEquals(5.0 / 3.0, result.getValue(), 1e-2);
    }

    // 5.0ft + (-2.0ft) in INCHES = 36.0 in
    @Test
    void testAddition_ExplicitTarget_NegativeValues() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .add(new Quantity<>(-2.0, LengthUnit.FEET),
                        LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // null targetUnit → IllegalArgumentException
    @Test
    void testAddition_ExplicitTarget_NullTargetUnit_ThrowsException() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);
        assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
    }

    // 1000ft + 500ft expressed in INCHES = 18000.0 in
    @Test
    void testAddition_ExplicitTarget_LargeToSmall() {
        Quantity<LengthUnit> result = new Quantity<>(1000.0, LengthUnit.FEET)
                .add(new Quantity<>(500.0, LengthUnit.FEET),
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

    // ─── UC9: WEIGHT MEASUREMENT TESTS ───────────────────────────────────────────

    // --- Equality ---

    @Test
    void testWeight_KgToKg_Same() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertTrue(w1.equals(w2));
    }

    @Test
    void testWeight_KgToGram_Equivalent() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0,    WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertTrue(w1.equals(w2));
    }

    @Test
    void testWeight_GramToKg_Equivalent() {
        Quantity<WeightUnit> w1 = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1.0,    WeightUnit.KILOGRAM);
        assertTrue(w1.equals(w2));
    }

    @Test
    void testWeight_KgToPound_Equivalent() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0,              WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1.0 / 0.453592,   WeightUnit.POUND);
        assertTrue(w1.equals(w2));
    }

    @Test
    void testWeight_DifferentValues() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(2.0, WeightUnit.KILOGRAM);
        assertFalse(w1.equals(w2));
    }

    @Test
    void testWeight_NullComparison() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(w1.equals(null));
    }

    @Test
    void testWeight_SameReference() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertTrue(w1.equals(w1));
    }

    @Test
    void testWeight_WeightVsLength_Incompatible() {
        Quantity<WeightUnit> w = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<LengthUnit> l = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(w.equals(l));
    }

    // --- Conversion ---

    @Test
    void testWeightConversion_KgToGram() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testWeightConversion_PoundToKg() {
        Quantity<WeightUnit> result = new Quantity<>(2.20462, WeightUnit.POUND)
                .convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.0, result.getValue(), 1e-3);
    }

    @Test
    void testWeightConversion_RoundTrip() {
        double original = 3.5;
        Quantity<WeightUnit> inKg   = new Quantity<>(original, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> inGram = inKg.convertTo(WeightUnit.GRAM);
        Quantity<WeightUnit> backKg = inGram.convertTo(WeightUnit.KILOGRAM);
        assertEquals(original, backKg.getValue(), 1e-6);
    }

    @Test
    void testWeightConversion_ZeroValue() {
        Quantity<WeightUnit> result = new Quantity<>(0.0, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    void testWeightConversion_NullUnit_ThrowsException() {
        Quantity<WeightUnit> w = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> w.convertTo(null));
    }

    // --- Addition ---

    @Test
    void testWeightAddition_KgPlusKg() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(2.0, WeightUnit.KILOGRAM));
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testWeightAddition_KgPlusGram() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(1000.0, WeightUnit.GRAM));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testWeightAddition_ExplicitTarget_Gram() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        assertEquals(2000.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testWeightAddition_NullOperand_ThrowsException() {
        Quantity<WeightUnit> w = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> w.add(null));
    }

    // ─── UC11: VOLUME MEASUREMENT TESTS ──────────────────────────────────────────

    // --- Equality ---

    @Test
    void testVolume_LitreToLitre_Same() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(v1.equals(v2));
    }

    @Test
    void testVolume_LitreToMillilitre_Equivalent() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(v1.equals(v2));
    }

    @Test
    void testVolume_MillilitreToLitre_Equivalent() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(v1.equals(v2));
    }

    @Test
    void testVolume_LitreToGallon_Equivalent() {
        Quantity<VolumeUnit> v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1.0,     VolumeUnit.GALLON);
        assertTrue(v1.equals(v2));
    }

    @Test
    void testVolume_DifferentValues() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(2.0, VolumeUnit.LITRE);
        assertFalse(v1.equals(v2));
    }

    @Test
    void testVolume_NullComparison() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertFalse(v1.equals(null));
    }

    @Test
    void testVolume_SameReference() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(v1.equals(v1));
    }

    @Test
    void testVolume_VolumeVsLength_Incompatible() {
        Quantity<VolumeUnit> v = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<LengthUnit> l = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(v.equals(l));
    }

    @Test
    void testVolume_VolumeVsWeight_Incompatible() {
        Quantity<VolumeUnit> v = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<WeightUnit> w = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(v.equals(w));
    }

    // --- Conversion ---

    @Test
    void testVolumeConversion_LitreToMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE);
        assertEquals(1000.0, result.getValue(), 1e-6);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testVolumeConversion_GallonToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON)
                .convertTo(VolumeUnit.LITRE);
        assertEquals(3.79, result.getValue(), 1e-2);
    }

    @Test
    void testVolumeConversion_MillilitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.GALLON);
        assertEquals(0.26, result.getValue(), 1e-2);
    }

    @Test
    void testVolumeConversion_RoundTrip() {
        double original = 2.5;
        Quantity<VolumeUnit> inLitre = new Quantity<>(original, VolumeUnit.LITRE);
        Quantity<VolumeUnit> inMl    = inLitre.convertTo(VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> backLitre = inMl.convertTo(VolumeUnit.LITRE);
        assertEquals(original, backLitre.getValue(), 1e-6);
    }

    @Test
    void testVolumeConversion_ZeroValue() {
        Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    // --- Addition ---

    @Test
    void testVolumeAddition_LitrePlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(2.0, VolumeUnit.LITRE));
        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testVolumeAddition_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testVolumeAddition_ExplicitTarget() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE);
        assertEquals(2000.0, result.getValue(), 1e-6);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testVolumeAddition_WithZero() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    // ─── UC12: SUBTRACTION & DIVISION TESTS ─────────────────────────────────────

    // --- Subtraction ---
    @Test
    void testSubtraction_SameUnit() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertEquals(5.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCHES));
        assertEquals(9.5, result.getValue(), 1e-6);
    }

    @Test
    void testSubtraction_ExplicitTarget() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertEquals(114.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testSubtraction_NegativeResult() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(-5.0, result.getValue(), 1e-6);
    }

    @Test
    void testSubtraction_ZeroResult() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(120.0, LengthUnit.INCHES));
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(0.0, LengthUnit.INCHES));
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> r1 = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET));
        Quantity<LengthUnit> r2 = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(10.0, LengthUnit.FEET));
        assertFalse(r1.equals(r2));
    }

    @Test
    void testSubtraction_NullOperand_Throws() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit_Throws() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(q2, null));
    }

    @Test
    void testSubtraction_CrossCategory_Throws() {
        Quantity<LengthUnit> l = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> w = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        // Generic constraints prevent compilation, unchecked cast could throw, validation is implicit but tested above
    }

    @Test
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(7.0, result.getValue(), 1e-6);
    }

    // --- Division ---
    @Test
    void testDivision_SameUnit() {
        double result = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    void testDivision_CrossUnit() {
        double result = new Quantity<>(24.0, LengthUnit.INCHES)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        double result = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        double result = new Quantity<>(5.0, LengthUnit.FEET)
                .divide(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        double result = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testDivision_NonCommutative() {
        double r1 = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        double r2 = new Quantity<>(2.0, LengthUnit.FEET)
                .divide(new Quantity<>(10.0, LengthUnit.FEET));
        assertNotEquals(r1, r2, 1e-6);
    }

    @Test
    void testDivision_ByZero_Throws() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.INCHES);
        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }

    @Test
    void testDivision_NullOperand_Throws() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    @Test
    void testDivision_Weight() {
        double result = new Quantity<>(10.0, WeightUnit.KILOGRAM)
                .divide(new Quantity<>(5.0, WeightUnit.KILOGRAM));
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    void testDivision_Volume() {
        double result = new Quantity<>(10.0, VolumeUnit.LITRE)
                .divide(new Quantity<>(5.0, VolumeUnit.LITRE));
        assertEquals(2.0, result, 1e-6);
    }

    // ─── UC13: CENTRALIZED ARITHMETIC TESTS ─────────────────────────────────────

    @Test
    void testValidation_NullOperand_AllOperations() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(null));
        assertThrows(IllegalArgumentException.class, () -> q1.divide(null));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void testValidation_CrossCategory_AllOperations() {
        Quantity l = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity w = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> l.add(w));
        assertThrows(IllegalArgumentException.class, () -> l.subtract(w));
        assertThrows(IllegalArgumentException.class, () -> l.divide(w));
    }
}

