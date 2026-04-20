package com.bridgelabz.service;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.QuantityMeasurementEntity;
import com.bridgelabz.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UC17: Service Unit Tests using Mockito (no Spring context).
 *
 * @ExtendWith(MockitoExtension.class):
 * - Pure Mockito — no Spring context started (very fast).
 * - @Transactional annotations are present but NOT enforced (no proxy wrapping).
 *
 * @Mock QuantityMeasurementRepository:
 * - Creates a Mockito mock. By default, repository.save() returns null (fine,
 *   since the service ignores the return value and uses the DTO directly).
 *
 * @InjectMocks QuantityMeasurementServiceImpl:
 * - Creates the real service, injecting the mock repository.
 *
 * These tests verify:
 * - Business logic correctness (conversions, comparisons, arithmetic)
 * - Error persistence: repository.save() is called even on failures
 * - Exception types and messages for domain violations
 * - Mockito.verify() confirms exact interaction counts
 */
@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    // =========================================================
    // Helper
    // =========================================================

    private QuantityDTO dto(double value, String unit, String type) {
        return new QuantityDTO(value, unit, type);
    }

    // =========================================================
    // Tests: compare()
    // =========================================================

    @Test
    @DisplayName("compare() — 1 FEET equals 12 INCHES → resultString = 'true'")
    void testCompare_Equal() {
        QuantityMeasurementDTO result = service.compare(
                dto(1.0, "FEET", "LengthUnit"),
                dto(12.0, "INCHES", "LengthUnit"));

        assertNotNull(result);
        assertEquals("compare", result.getOperation());
        assertEquals("true", result.getResultString());
        assertFalse(result.isError());
        // SUCCESS entity is persisted
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    @DisplayName("compare() — 1 FEET does not equal 2 FEET → resultString = 'false'")
    void testCompare_NotEqual() {
        QuantityMeasurementDTO result = service.compare(
                dto(1.0, "FEET", "LengthUnit"),
                dto(2.0, "FEET", "LengthUnit"));

        assertEquals("false", result.getResultString());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("compare() — incompatible types → QuantityMeasurementException + error entity saved")
    void testCompare_IncompatibleTypes_ThrowsAndSavesError() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.compare(
                        dto(1.0, "FEET",     "LengthUnit"),
                        dto(1.0, "KILOGRAM", "WeightUnit"))
        );
        // Error entity MUST be persisted (noRollbackFor behaviour)
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    @DisplayName("compare() — invalid measurementType → QuantityMeasurementException")
    void testCompare_InvalidMeasurementType_ThrowsException() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.compare(
                        dto(1.0, "FEET", "InvalidType"),
                        dto(1.0, "FEET", "InvalidType"))
        );
    }

    // =========================================================
    // Tests: convert()
    // =========================================================

    @Test
    @DisplayName("convert() — 1 FEET to INCHES → resultValue = 12.0")
    void testConvert_FeetToInches() {
        QuantityMeasurementDTO result = service.convert(
                dto(1.0, "FEET",   "LengthUnit"),
                dto(0.0, "INCHES", "LengthUnit"));

        assertEquals(12.0, result.getResultValue(), 0.001);
        assertEquals("INCHES", result.getResultUnit());
        assertEquals("convert", result.getOperation());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("convert() — 100 CELSIUS to FAHRENHEIT → resultValue = 212.0")
    void testConvert_CelsiusToFahrenheit() {
        QuantityMeasurementDTO result = service.convert(
                dto(100.0, "CELSIUS",    "TemperatureUnit"),
                dto(0.0,   "FAHRENHEIT", "TemperatureUnit"));

        assertEquals(212.0, result.getResultValue(), 0.01);
    }

    @Test
    @DisplayName("convert() — 1 KILOGRAM to GRAM → resultValue = 1000.0")
    void testConvert_KilogramToGram() {
        QuantityMeasurementDTO result = service.convert(
                dto(1.0, "KILOGRAM", "WeightUnit"),
                dto(0.0, "GRAM",     "WeightUnit"));

        assertEquals(1000.0, result.getResultValue(), 0.001);
    }

    // =========================================================
    // Tests: add()
    // =========================================================

    @Test
    @DisplayName("add() — 1 FEET + 12 INCHES → resultValue = 2.0 FEET")
    void testAdd_FeetAndInches() {
        QuantityMeasurementDTO result = service.add(
                dto(1.0,  "FEET",   "LengthUnit"),
                dto(12.0, "INCHES", "LengthUnit"));

        assertEquals(2.0, result.getResultValue(), 0.001);
        assertEquals("FEET", result.getResultUnit());
        assertEquals("add", result.getOperation());
        assertFalse(result.isError());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("add() — 1 KILOGRAM + 1000 GRAM → resultValue = 2.0 KILOGRAM")
    void testAdd_KilogramAndGram() {
        QuantityMeasurementDTO result = service.add(
                dto(1.0,    "KILOGRAM", "WeightUnit"),
                dto(1000.0, "GRAM",     "WeightUnit"));

        assertEquals(2.0, result.getResultValue(), 0.001);
        assertEquals("KILOGRAM", result.getResultUnit());
    }

    @Test
    @DisplayName("add() — incompatible types → QuantityMeasurementException + error entity saved")
    void testAdd_IncompatibleTypes_ThrowsAndSavesError() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.add(
                        dto(1.0, "FEET",     "LengthUnit"),
                        dto(1.0, "KILOGRAM", "WeightUnit"))
        );
        // Error entity MUST be saved even on domain exception (noRollbackFor)
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // =========================================================
    // Tests: subtract()
    // =========================================================

    @Test
    @DisplayName("subtract() — 2 FEET - 12 INCHES → resultValue = 1.0 FEET")
    void testSubtract_FeetMinusInches() {
        QuantityMeasurementDTO result = service.subtract(
                dto(2.0,  "FEET",   "LengthUnit"),
                dto(12.0, "INCHES", "LengthUnit"));

        assertEquals(1.0, result.getResultValue(), 0.001);
        assertEquals("FEET", result.getResultUnit());
    }

    // =========================================================
    // Tests: divide()
    // =========================================================

    @Test
    @DisplayName("divide() — divide by zero → QuantityMeasurementException + error entity saved")
    void testDivide_ByZero_ThrowsAndSavesError() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.divide(
                        dto(1.0, "FEET",   "LengthUnit"),
                        dto(0.0, "INCHES", "LengthUnit"))  // 0 INCHES = 0 base value
        );
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // =========================================================
    // Tests: getHistoryByOperation()
    // =========================================================

    @Test
    @DisplayName("getHistoryByOperation() — returns list from repository")
    void testGetHistoryByOperation_ReturnsList() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setOperation("compare");
        entity.setThisValue(1.0);
        entity.setThisUnit("FEET");
        entity.setThisMeasurementType("LengthUnit");
        entity.setThatValue(12.0);
        entity.setThatUnit("INCHES");
        entity.setThatMeasurementType("LengthUnit");
        entity.setError(false);

        when(repository.findByOperation("compare")).thenReturn(List.of(entity));

        List<QuantityMeasurementDTO> result = service.getHistoryByOperation("compare");

        assertFalse(result.isEmpty());
        assertEquals("compare", result.get(0).getOperation());
        verify(repository, times(1)).findByOperation("compare");
    }

    @Test
    @DisplayName("getHistoryByOperation() — returns empty list when no records")
    void testGetHistoryByOperation_ReturnsEmpty() {
        when(repository.findByOperation("add")).thenReturn(Collections.emptyList());

        List<QuantityMeasurementDTO> result = service.getHistoryByOperation("add");

        assertTrue(result.isEmpty());
    }

    // =========================================================
    // Tests: getHistoryByMeasurementType()
    // =========================================================

    @Test
    @DisplayName("getHistoryByMeasurementType() — returns list for matching type")
    void testGetHistoryByMeasurementType() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisMeasurementType("LengthUnit");
        entity.setOperation("add");
        entity.setThisValue(1.0);
        entity.setThisUnit("FEET");
        entity.setError(false);

        when(repository.findByThisMeasurementType("LengthUnit")).thenReturn(List.of(entity));

        List<QuantityMeasurementDTO> result = service.getHistoryByMeasurementType("LengthUnit");

        assertEquals(1, result.size());
        assertEquals("LengthUnit", result.get(0).getThisMeasurementType());
    }

    // =========================================================
    // Tests: getCountByOperation()
    // =========================================================

    @Test
    @DisplayName("getCountByOperation() — returns count from repository")
    void testGetCountByOperation() {
        when(repository.countByOperationAndErrorFalse("compare")).thenReturn(7L);

        long count = service.getCountByOperation("compare");

        assertEquals(7L, count);
        verify(repository, times(1)).countByOperationAndErrorFalse("compare");
    }

    @Test
    @DisplayName("getCountByOperation() — returns 0 when no successful records")
    void testGetCountByOperation_ReturnsZero() {
        when(repository.countByOperationAndErrorFalse("divide")).thenReturn(0L);

        assertEquals(0L, service.getCountByOperation("divide"));
    }

    // =========================================================
    // Tests: getErrorHistory()
    // =========================================================

    @Test
    @DisplayName("getErrorHistory() — returns all errored records")
    void testGetErrorHistory_ReturnsList() {
        QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity();
        errorEntity.setOperation("add");
        errorEntity.setError(true);
        errorEntity.setErrorMessage("Cannot operate between different types");
        errorEntity.setThisValue(1.0);
        errorEntity.setThisUnit("FEET");
        errorEntity.setThisMeasurementType("LengthUnit");

        when(repository.findByErrorTrue()).thenReturn(List.of(errorEntity));

        List<QuantityMeasurementDTO> errors = service.getErrorHistory();

        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).isError());
        assertNotNull(errors.get(0).getErrorMessage());
        verify(repository, times(1)).findByErrorTrue();
    }

    @Test
    @DisplayName("getErrorHistory() — returns empty list when no errors")
    void testGetErrorHistory_ReturnsEmpty() {
        when(repository.findByErrorTrue()).thenReturn(Collections.emptyList());

        assertTrue(service.getErrorHistory().isEmpty());
    }
}
