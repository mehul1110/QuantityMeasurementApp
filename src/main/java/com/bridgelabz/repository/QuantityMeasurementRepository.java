package com.bridgelabz.repository;

import com.bridgelabz.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UC17: Spring Data JPA Repository for QuantityMeasurementEntity.
 *
 * Replaces the manual JDBC-based IQuantityMeasurementRepository from UC16.
 * Extending JpaRepository<QuantityMeasurementEntity, Long> provides:
 *   - save(), findById(), findAll(), deleteById(), count() — out of the box.
 *
 * Additional query methods are derived from method naming conventions,
 * or defined using @Query for custom JPQL.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    /**
     * Find all measurements by operation type (case-sensitive).
     * Spring Data generates: SELECT * FROM quantity_measurements WHERE operation = ?
     *
     * @param operation e.g. "compare", "convert", "add", "subtract", "divide"
     * @return list of matching entities
     */
    List<QuantityMeasurementEntity> findByOperation(String operation);

    /**
     * Find all measurements where thisMeasurementType matches the given value.
     * Spring Data generates: SELECT * FROM quantity_measurements WHERE thisMeasurementType = ?
     *
     * @param measurementType e.g. "LengthUnit", "WeightUnit"
     * @return list of matching entities
     */
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    /**
     * Find all measurements created after a given date/time.
     * Spring Data generates: SELECT * FROM quantity_measurements WHERE createdAt > ?
     */
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Custom JPQL query: find all successful (non-error) measurements for a given operation.
     *
     * @param operation the operation type
     * @return list of successful entities
     */
    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = :operation AND q.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    /**
     * Count the number of successful records for a given operation.
     * Spring Data generates: SELECT COUNT(*) FROM ... WHERE operation = ? AND error = false
     *
     * @param operation the operation type
     * @return count
     */
    long countByOperationAndErrorFalse(String operation);

    /**
     * Find all errored measurement records.
     * Spring Data generates: SELECT * FROM quantity_measurements WHERE error = true
     *
     * @return list of error entities
     */
    List<QuantityMeasurementEntity> findByErrorTrue();
}
