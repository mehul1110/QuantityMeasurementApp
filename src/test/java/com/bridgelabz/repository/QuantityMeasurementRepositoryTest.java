package com.bridgelabz.repository;

import com.bridgelabz.model.QuantityMeasurementEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UC17: Repository Unit Tests using @DataJpaTest.
 *
 * @DataJpaTest:
 * - Creates a minimal Spring context with ONLY JPA components (no web layer, no services).
 * - Auto-configures an in-memory H2 database for isolation.
 * - Auto-rolls back each test → clean DB state per test.
 *
 * Tests verify that all derived query methods and @Query annotations in
 * QuantityMeasurementRepository work correctly against real JPA operations.
 */
@DataJpaTest
class QuantityMeasurementRepositoryTest {

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // =========================================================
    // Helper: Build a test entity
    // =========================================================

    private QuantityMeasurementEntity entity(String operation, String thisMType, boolean error) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        e.setThisValue(1.0);
        e.setThisUnit("FEET");
        e.setThisMeasurementType(thisMType);
        e.setThatValue(12.0);
        e.setThatUnit("INCHES");
        e.setThatMeasurementType(thisMType);
        e.setOperation(operation.toLowerCase());
        e.setResultValue(1.0);
        e.setResultString("true");
        e.setError(error);
        if (error) e.setErrorMessage("Test error: " + operation);
        return e;
    }

    // =========================================================
    // CRUD Tests
    // =========================================================

    @Test
    @DisplayName("save() — assigns auto-generated ID and persists entity")
    void shouldSaveEntityAndAssignId() {
        QuantityMeasurementEntity saved = repository.save(entity("compare", "LengthUnit", false));

        assertNotNull(saved.getId());
        assertTrue(saved.getId() > 0);
    }

    @Test
    @DisplayName("findById() — retrieves the saved entity")
    void shouldFindEntityById() {
        QuantityMeasurementEntity saved = repository.save(entity("compare", "LengthUnit", false));

        Optional<QuantityMeasurementEntity> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("compare", found.get().getOperation());
    }

    @Test
    @DisplayName("@PrePersist — assigns createdAt and updatedAt on save")
    void shouldAssignTimestampsOnPersist() {
        QuantityMeasurementEntity saved = repository.save(entity("compare", "LengthUnit", false));

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    // =========================================================
    // Derived Query Method Tests
    // =========================================================

    @Test
    @DisplayName("findByOperation() — returns only entities matching the given operation")
    void shouldFindByOperation() {
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("add",     "LengthUnit", false));

        List<QuantityMeasurementEntity> results = repository.findByOperation("compare");

        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        results.forEach(r -> assertEquals("compare", r.getOperation()));
    }

    @Test
    @DisplayName("findByOperation() — returns empty list when no match")
    void shouldReturnEmptyListForUnknownOperation() {
        repository.save(entity("compare", "LengthUnit", false));

        List<QuantityMeasurementEntity> results = repository.findByOperation("divide");

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("findByThisMeasurementType() — returns entities matching measurement type")
    void shouldFindByThisMeasurementType() {
        repository.save(entity("compare", "LengthUnit",  false));
        repository.save(entity("compare", "WeightUnit",  false));
        repository.save(entity("compare", "LengthUnit",  false));

        List<QuantityMeasurementEntity> results = repository.findByThisMeasurementType("LengthUnit");

        assertEquals(2, results.size());
        results.forEach(r -> assertEquals("LengthUnit", r.getThisMeasurementType()));
    }

    @Test
    @DisplayName("findByErrorTrue() — returns only error records")
    void shouldFindByErrorTrue() {
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("add",     "LengthUnit", true));
        repository.save(entity("convert", "LengthUnit", true));

        List<QuantityMeasurementEntity> errors = repository.findByErrorTrue();

        assertEquals(2, errors.size());
        errors.forEach(e -> assertTrue(e.isError()));
    }

    @Test
    @DisplayName("findByErrorTrue() — returns empty when no errors exist")
    void shouldReturnEmptyWhenNoErrors() {
        repository.save(entity("compare", "LengthUnit", false));

        List<QuantityMeasurementEntity> errors = repository.findByErrorTrue();

        assertTrue(errors.isEmpty());
    }

    // =========================================================
    // Derived Count Query Tests
    // =========================================================

    @Test
    @DisplayName("countByOperationAndErrorFalse() — counts only successful (non-error) records")
    void shouldCountSuccessfulByOperation() {
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("compare", "LengthUnit", true));  // error — should NOT be counted

        long count = repository.countByOperationAndErrorFalse("compare");

        assertEquals(2L, count);
    }

    @Test
    @DisplayName("countByOperationAndErrorFalse() — returns 0 when no successes")
    void shouldReturnZeroCountForEmptySuccesses() {
        repository.save(entity("compare", "LengthUnit", true)); // only errors

        long count = repository.countByOperationAndErrorFalse("compare");

        assertEquals(0L, count);
    }

    // =========================================================
    // Custom @Query Tests
    // =========================================================

    @Test
    @DisplayName("findSuccessfulByOperation() (@Query) — returns only non-error records for operation")
    void shouldFindSuccessfulByOperation() {
        repository.save(entity("add", "LengthUnit", false));
        repository.save(entity("add", "LengthUnit", false));
        repository.save(entity("add", "LengthUnit", true));   // error — should be excluded

        List<QuantityMeasurementEntity> results = repository.findSuccessfulByOperation("add");

        assertEquals(2, results.size());
        results.forEach(r -> {
            assertFalse(r.isError());
            assertEquals("add", r.getOperation());
        });
    }

    @Test
    @DisplayName("findSuccessfulByOperation() — returns empty when all operations errored")
    void shouldReturnEmptySuccessfulForAllErrors() {
        repository.save(entity("add", "LengthUnit", true));

        List<QuantityMeasurementEntity> results = repository.findSuccessfulByOperation("add");

        assertTrue(results.isEmpty());
    }

    // =========================================================
    // Data Integrity Tests
    // =========================================================

    @Test
    @DisplayName("deleteAll() — clears all records")
    void shouldDeleteAllRecords() {
        repository.save(entity("compare", "LengthUnit", false));
        repository.save(entity("add", "WeightUnit", false));

        repository.deleteAll();

        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("save() — errorMessage is null for success records")
    void shouldHaveNullErrorMessageForSuccessRecords() {
        QuantityMeasurementEntity saved = repository.save(entity("compare", "LengthUnit", false));

        assertNull(saved.getErrorMessage());
        assertFalse(saved.isError());
    }
}
