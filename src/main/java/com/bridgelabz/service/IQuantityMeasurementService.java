package com.bridgelabz.service;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;

import java.util.List;

/**
 * UC17: Updated service interface for quantity measurement operations.
 *
 * Key changes from UC16:
 * - All operation methods now return QuantityMeasurementDTO instead of QuantityDTO,
 *   providing richer response data (measurement types, operation, result info, error).
 * - New history/count retrieval methods for API endpoints.
 * - Takes String-based QuantityDTO objects (unit and measurementType as Strings).
 */
public interface IQuantityMeasurementService {

    /**
     * Compare two quantities of the same measurement type.
     *
     * @param thisDto first quantity
     * @param thatDto second quantity
     * @return result DTO with resultString = "true" or "false"
     */
    QuantityMeasurementDTO compare(QuantityDTO thisDto, QuantityDTO thatDto);

    /**
     * Convert thisDto to the unit specified in thatDto.
     *
     * @param thisDto source quantity
     * @param thatDto target unit specification (value ignored, unit used as target)
     * @return result DTO with resultValue = converted quantity
     */
    QuantityMeasurementDTO convert(QuantityDTO thisDto, QuantityDTO thatDto);

    /**
     * Add two quantities. The result unit is the same as thisDto's unit.
     *
     * @param thisDto first operand
     * @param thatDto second operand
     * @return result DTO with resultValue = sum, resultUnit = thisDto.unit
     */
    QuantityMeasurementDTO add(QuantityDTO thisDto, QuantityDTO thatDto);

    /**
     * Subtract thatDto from thisDto. The result unit is thisDto's unit.
     *
     * @param thisDto first operand
     * @param thatDto second operand (subtracted from first)
     * @return result DTO with resultValue = difference
     */
    QuantityMeasurementDTO subtract(QuantityDTO thisDto, QuantityDTO thatDto);

    /**
     * Divide thisDto by thatDto (returns dimensionless ratio).
     *
     * @param thisDto dividend
     * @param thatDto divisor
     * @return result DTO with resultValue = ratio
     */
    QuantityMeasurementDTO divide(QuantityDTO thisDto, QuantityDTO thatDto);

    /**
     * Retrieve all measurement records for a specific operation type.
     *
     * @param operation e.g. "compare", "convert", "add"
     * @return list of matching QuantityMeasurementDTOs
     */
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    /**
     * Retrieve all measurement records for a specific measurement type.
     *
     * @param measurementType e.g. "LengthUnit", "WeightUnit"
     * @return list of matching QuantityMeasurementDTOs
     */
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    /**
     * Count the number of successful (non-error) operations for a given type.
     *
     * @param operation the operation type
     * @return count of successful operations
     */
    long getCountByOperation(String operation);

    /**
     * Retrieve all errored measurement records.
     *
     * @return list of error QuantityMeasurementDTOs
     */
    List<QuantityMeasurementDTO> getErrorHistory();
}
