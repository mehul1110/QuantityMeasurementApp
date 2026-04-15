package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.exception.DatabaseException;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuantityMeasurementDatabaseRepositoryTest {

    private QuantityMeasurementDatabaseRepository repository;

    @BeforeAll
    void setup() {
        // This will use the quantitydb defined in application.properties
        repository = new QuantityMeasurementDatabaseRepository();
    }

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void testSave_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true");
        repository.save(entity);
        assertEquals(1, repository.count());
    }

    @Test
    void testGetAllMeasurements_Success() {
        repository.save(new QuantityMeasurementEntity("CONVERT", "1.0 FEET", "12.0 INCHES"));
        repository.save(new QuantityMeasurementEntity("ADD", "1.0 FEET", "1.0 FEET", "2.0 FEET"));
        
        List<QuantityMeasurementEntity> measurements = repository.getAllMeasurements();
        assertEquals(2, measurements.size());
    }

    @Test
    void testFindByOperationType_Success() {
        repository.save(new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true"));
        repository.save(new QuantityMeasurementEntity("ADD", "1.0 FEET", "1.0 FEET", "2.0 FEET"));
        
        List<QuantityMeasurementEntity> compareStats = repository.findByOperationType("COMPARE");
        assertEquals(1, compareStats.size());
        assertEquals("COMPARE", compareStats.get(0).getOperationType());
    }

    @Test
    void testCount_Success() {
        assertEquals(0, repository.count());
        repository.save(new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true"));
        assertEquals(1, repository.count());
    }

    @Test
    void testDeleteAll_Success() {
        repository.save(new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true"));
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @AfterAll
    void tearDown() {
        repository.close();
    }
}
