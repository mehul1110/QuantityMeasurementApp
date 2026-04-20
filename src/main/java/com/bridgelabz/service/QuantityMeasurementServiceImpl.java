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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UC17: Spring-managed Service Implementation.
 *
 * Transaction Management:
 * ─────────────────────────────────────────────────────────────────
 * @Transactional(noRollbackFor = QuantityMeasurementException.class)
 *   Applied to all mutation methods (compare, convert, add, subtract, divide).
 *   Both SUCCESS records and ERROR records are persisted even when domain
 *   exceptions occur — because @Transactional does NOT rollback for
 *   QuantityMeasurementException. This gives us a complete audit trail.
 *
 * @Transactional(readOnly = true)
 *   Applied to all read-only methods (history, count queries).
 *   Instructs Hibernate to skip dirty checking → better performance.
 * ─────────────────────────────────────────────────────────────────
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    @Autowired
    private QuantityMeasurementRepository repository;

    // =========================================================
    // Private Helpers
    // =========================================================

    /**
     * Maps String-based measurementType ("LengthUnit") + unit ("FEET")
     * from the API DTO to a typed QuantityModel using the IMeasurable enum.
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
                        "Invalid measurement type: " + dto.getMeasurementType());
        }
        try {
            IMeasurable unit = reference.getUnitByName(dto.getUnit());
            return new QuantityModel<>(dto.getValue(), unit);
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException(
                    "Invalid unit '" + dto.getUnit() + "' for type " + dto.getMeasurementType());
        }
    }

    /** Validates both DTOs belong to the same measurement category. */
    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2, String operationName) {
        if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    operationName + " Error: Cannot operate between different categories: "
                    + q1.getMeasurementType() + " vs " + q2.getMeasurementType());
        }
    }

    /** Builds a base QuantityMeasurementDTO from both input DTOs. */
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

    /**
     * Persists an error record to the DB.
     * Called from catch blocks — because noRollbackFor is set, this
     * commit goes through even when an exception is being propagated.
     */
    private void saveErrorEntity(QuantityDTO thisDto, QuantityDTO thatDto,
                                 String operation, String errorMessage) {
        QuantityMeasurementDTO errorDto = buildBaseDTO(thisDto, thatDto, operation);
        errorDto.setError(true);
        errorDto.setErrorMessage(errorMessage);
        repository.save(errorDto.toEntity());
        log.debug("Error entity saved for operation '{}': {}", operation, errorMessage);
    }

    // =========================================================
    // Mutation Operations (@Transactional with noRollbackFor)
    //   — SUCCESS result AND ERROR records both commit to DB.
    // =========================================================

    @Override
    @Transactional(noRollbackFor = QuantityMeasurementException.class)
    public QuantityMeasurementDTO compare(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "compare");

            Quantity<IMeasurable> q1 = toQuantity(convertDtoToModel(thisDto));
            Quantity<IMeasurable> q2 = toQuantity(convertDtoToModel(thatDto));
            boolean isEqual = q1.equals(q2);

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.COMPARE.toOperationString());
            resultDto.setResultString(String.valueOf(isEqual));
            resultDto.setResultValue(0.0);

            repository.save(resultDto.toEntity());
            log.debug("Compare: {} {} vs {} {} = {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(), isEqual);
            return resultDto;

        } catch (QuantityMeasurementException e) {
            // Domain exception — save error record and re-throw (no double-wrap)
            saveErrorEntity(thisDto, thatDto, OperationType.COMPARE.toOperationString(), e.getMessage());
            throw e;
        } catch (Exception e) {
            // System exception — wrap in domain exception
            log.error("Compare failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.COMPARE.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(noRollbackFor = QuantityMeasurementException.class)
    public QuantityMeasurementDTO convert(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "convert");

            QuantityModel<IMeasurable> sourceModel = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> targetModel  = convertDtoToModel(thatDto);
            Quantity<IMeasurable> converted = toQuantity(sourceModel).convertTo(targetModel.getUnit());

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.CONVERT.toOperationString());
            resultDto.setResultValue(converted.getValue());
            resultDto.setResultUnit(thatDto.getUnit());
            resultDto.setResultMeasurementType(thatDto.getMeasurementType());

            repository.save(resultDto.toEntity());
            log.debug("Convert: {} {} → {} {}", thisDto.getValue(), thisDto.getUnit(),
                    converted.getValue(), thatDto.getUnit());
            return resultDto;

        } catch (QuantityMeasurementException e) {
            saveErrorEntity(thisDto, thatDto, OperationType.CONVERT.toOperationString(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Convert failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.CONVERT.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(noRollbackFor = QuantityMeasurementException.class)
    public QuantityMeasurementDTO add(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "add");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);
            Quantity<IMeasurable> sum = toQuantity(m1).add(toQuantity(m2), m1.getUnit());

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

        } catch (QuantityMeasurementException e) {
            saveErrorEntity(thisDto, thatDto, OperationType.ADD.toOperationString(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Add failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.ADD.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(noRollbackFor = QuantityMeasurementException.class)
    public QuantityMeasurementDTO subtract(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "subtract");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);
            Quantity<IMeasurable> diff = toQuantity(m1).subtract(toQuantity(m2), m1.getUnit());

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

        } catch (QuantityMeasurementException e) {
            saveErrorEntity(thisDto, thatDto, OperationType.SUBTRACT.toOperationString(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Subtract failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.SUBTRACT.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(noRollbackFor = QuantityMeasurementException.class)
    public QuantityMeasurementDTO divide(QuantityDTO thisDto, QuantityDTO thatDto) {
        try {
            validateSameCategory(thisDto, thatDto, "divide");

            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisDto);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatDto);
            double ratio = toQuantity(m1).divide(toQuantity(m2));

            QuantityMeasurementDTO resultDto = buildBaseDTO(thisDto, thatDto,
                    OperationType.DIVIDE.toOperationString());
            resultDto.setResultValue(ratio);

            repository.save(resultDto.toEntity());
            log.debug("Divide: {} {} / {} {} = {}",
                    thisDto.getValue(), thisDto.getUnit(),
                    thatDto.getValue(), thatDto.getUnit(), ratio);
            return resultDto;

        } catch (QuantityMeasurementException e) {
            saveErrorEntity(thisDto, thatDto, OperationType.DIVIDE.toOperationString(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Divide failed: {}", e.getMessage());
            saveErrorEntity(thisDto, thatDto, OperationType.DIVIDE.toOperationString(), e.getMessage());
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    // =========================================================
    // Read-Only Queries (@Transactional readOnly = true)
    //   — Hibernate skips dirty checking; better read performance.
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        log.debug("Fetching history for operation: {}", operation);
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByOperation(operation.toLowerCase()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        log.debug("Fetching history for measurement type: {}", measurementType);
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByThisMeasurementType(measurementType));
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountByOperation(String operation) {
        log.debug("Counting successful '{}' operations", operation);
        return repository.countByOperationAndErrorFalse(operation.toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementDTO> getErrorHistory() {
        log.debug("Fetching all errored measurements");
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }

    // =========================================================
    // Utility
    // =========================================================

    /** Converts a QuantityModel to a Quantity instance. */
    private Quantity<IMeasurable> toQuantity(QuantityModel<IMeasurable> model) {
        return new Quantity<>(model.getValue(), model.getUnit());
    }
}
