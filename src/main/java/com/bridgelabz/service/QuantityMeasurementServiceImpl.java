package com.bridgelabz.service;

import com.bridgelabz.dto.OperationType;
import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.*;
import com.bridgelabz.repository.QuantityMeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UC17: Spring-managed Service Implementation.
 *
 * Key UC17 enhancements over UC16:
 * 1. Annotated with @Service — registered as a Spring bean.
 * 2. @Autowired field injection for QuantityMeasurementRepository (Spring Data JPA).
 * 3. No @Transactional — results AND errors are persisted to DB even if exceptions occur.
 * 4. All operation methods return QuantityMeasurementDTO instead of QuantityDTO.
 * 5. convertDtoToModel() replaces the old getQuantityModel()/toModel() method.
 * 6. New history/count API methods added.
 * 7. No manual connection pool or JDBC code — all handled by Spring Data JPA.
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    /**
     * Spring Data JPA repository — injected by Spring container.
     * No constructor needed; @Autowired handles dependency injection.
     */
    @Autowired
    private QuantityMeasurementRepository repository;

    // =========================================================
    // Private Helper: Convert QuantityDTO → QuantityModel
    // =========================================================

    /**
     * Converts a QuantityDTO (API layer) to a QuantityModel (business layer).
     *
     * Maps String-based measurementType ("LengthUnit") to the corresponding
     * IMeasurable enum reference, then resolves the unit by name.
     *
     * @param dto the input QuantityDTO from the API
     * @return QuantityModel wrapping value + resolved IMeasurable unit
     */
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable reference;
        switch (dto.getMeasurementType()) {
            case "LengthUnit":      reference = LengthUnit.FEET;         break;
            case "WeightUnit":      reference = WeightUnit.KILOGRAM;      break;
            case "VolumeUnit":      reference = VolumeUnit.LITER;         break;
            case "TemperatureUnit": reference = TemperatureUnit.CELSIUS;  break;
            default:
                throw new QuantityMeasurementException(
                        "Unknown measurement type: " + dto.getMeasurementType());
        }
        IMeasurable unit = reference.getUnitByName(dto.getUnit());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    /**
     * Validates that both DTOs belong to the same measurement category.
     */
    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2, String operationName) {
        if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    operationName + " Error: Cannot perform arithmetic between different" +
                    " measurement categories: " + q1.getMeasurementType() +
                    " and " + q2.getMeasurementType());
        }
    }

    // =========================================================
    // Helper: Build a base entity from the two input DTOs
    // =========================================================
    private QuantityMeasurementDTO buildBaseDTO(QuantityDTO thisDto, QuantityDTO thatDto,
                                                 String operation) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(thisDto.getValue());
        dto.setThisUnit(thisDto.getUnit());
        dto.setThisMeasurementType(thisDto.getMeasurementType());
        dto.setThatValue(thatDto.getValue());
        dto.setThatUnit(thatDto.getUnit());
        dto.setThatMeasurementType(thatDto.getMeasurementType());
        dto.setOperation(operation);
        dto.setError(false);
        return dto;
    }

    // =========================================================
    // Helper: Build an error entity and save to repository
    // =========================================================
    private void saveErrorEntity(QuantityDTO thisDto, QuantityDTO thatDto,
                                  String operation, String errorMessage) {
        QuantityMeasurementDTO errorDto = buildBaseDTO(thisDto, thatDto, operation);
        errorDto.setError(true);
        errorDto.setErrorMessage(errorMessage);
        repository.save(errorDto.toEntity());
    }

    // =========================================================
    // Operation Methods
    // =========================================================

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "compare");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);

            Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

            boolean isEqual = q1.equals(q2);

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.COMPARE.toOperationString());
            resultDto.setResultString(String.valueOf(isEqual));
            resultDto.setResultValue(0.0);

            repository.save(resultDto.toEntity());
            log.debug("Compare result: {} {} vs {} {} = {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(), isEqual);
            return resultDto;

        } catch (Exception e) {
            log.error("Compare failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.COMPARE.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "convert");

            QuantityModel<IMeasurable> sourceModel = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> targetModel  = convertDtoToModel(thatDto);

            Quantity<IMeasurable> sourceQty = new Quantity<>(sourceModel.getValue(), sourceModel.getUnit());
            Quantity<IMeasurable> converted = sourceQty.convertTo(targetModel.getUnit());

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.CONVERT.toOperationString());
            resultDto.setResultValue(converted.getValue());
            resultDto.setResultUnit(thatDto.getUnit());
            resultDto.setResultMeasurementType(thatDto.getMeasurementType());

            repository.save(resultDto.toEntity());
            log.debug("Convert: {} {} → {} {}", thisDto.getValue(), thisDto.getUnit(),
                    converted.getValue(), thatDto.getUnit());
            return resultDto;

        } catch (Exception e) {
            log.error("Convert failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.CONVERT.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "add");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);

            Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

            // Result unit = first operand's unit
            Quantity<IMeasurable> sum = q1.add(q2, m1.getUnit());

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.ADD.toOperationString());
            resultDto.setResultValue(sum.getValue());
            resultDto.setResultUnit(thisDto.getUnit());
            resultDto.setResultMeasurementType(thisDto.getMeasurementType());

            repository.save(resultDto.toEntity());
            log.debug("Add: {} {} + {} {} = {} {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(),
                    sum.getValue(), thisDto.getUnit());
            return resultDto;

        } catch (Exception e) {
            log.error("Add failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.ADD.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "subtract");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);

            Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

            Quantity<IMeasurable> diff = q1.subtract(q2, m1.getUnit());

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.SUBTRACT.toOperationString());
            resultDto.setResultValue(diff.getValue());
            resultDto.setResultUnit(thisDto.getUnit());
            resultDto.setResultMeasurementType(thisDto.getMeasurementType());

            repository.save(resultDto.toEntity());
            log.debug("Subtract: {} {} - {} {} = {} {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(),
                    diff.getValue(), thisDto.getUnit());
            return resultDto;

        } catch (Exception e) {
            log.error("Subtract failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.SUBTRACT.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "divide");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);

            Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

            double ratio = q1.divide(q2);

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.DIVIDE.toOperationString());
            resultDto.setResultValue(ratio);

            repository.save(resultDto.toEntity());
            log.debug("Divide: {} {} / {} {} = {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(), ratio);
            return resultDto;

        } catch (Exception e) {
            log.error("Divide failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.DIVIDE.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    // =========================================================
    // History & Count APIs (new in UC17)
    // =========================================================

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        log.debug("Fetching history for operation: {}", operation);
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByOperation(operation.toLowerCase()));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        log.debug("Fetching history for measurement type: {}", measurementType);
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public long getCountByOperation(String operation) {
        log.debug("Counting successful operations of type: {}", operation);
        return repository.countByOperationAndErrorFalse(operation.toLowerCase());
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        log.debug("Fetching all errored measurements");
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }
}
