package com.bridgelabz.service;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.*;
import com.bridgelabz.repository.IQuantityMeasurementRepository;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new QuantityMeasurementException("Repository cannot be null");
        }
        this.repository = repository;
    }

    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        IMeasurable reference;
        switch (dto.getMeasurementType()) {
            case "LENGTH": reference = LengthUnit.FEET; break;
            case "WEIGHT": reference = WeightUnit.GRAM; break;
            case "VOLUME": reference = VolumeUnit.MILLILITER; break;
            case "TEMPERATURE": reference = TemperatureUnit.CELSIUS; break;
            default: throw new QuantityMeasurementException("Unknown measurement type: " + dto.getMeasurementType());
        }
        IMeasurable unit = reference.getUnitByName(dto.getUnitName());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2, String operationName) {
        if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
            throw new QuantityMeasurementException("Cannot perform " + operationName + " on different categories: " 
                    + q1.getMeasurementType() + " and " + q2.getMeasurementType());
        }
    }

    private void validateNotNull(Object obj, String name) {
        if (obj == null) {
            throw new QuantityMeasurementException(name + " cannot be null");
        }
    }

    @Override
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {
        try {
            validateNotNull(q1, "Operand 1");
            validateNotNull(q2, "Operand 2");
            validateSameCategory(q1, q2, "COMPARE");

            QuantityModel<IMeasurable> m1 = toModel(q1);
            QuantityModel<IMeasurable> m2 = toModel(q2);

            Quantity<IMeasurable> quantity1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> quantity2 = new Quantity<>(m2.getValue(), m2.getUnit());

            boolean isEqual = quantity1.equals(quantity2);
            QuantityDTO result = new QuantityDTO(isEqual ? 1.0 : 0.0, QuantityDTO.ResultUnit.BOOLEAN);
            
            repository.save(new QuantityMeasurementEntity("COMPARE", q1.toString(), q2.toString(), String.valueOf(isEqual)));
            return result;
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("COMPARE", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO source, QuantityDTO target) {
        try {
            validateNotNull(source, "Source");
            validateNotNull(target, "Target");
            validateSameCategory(source, target, "CONVERT");

            QuantityModel<IMeasurable> sModel = toModel(source);
            QuantityModel<IMeasurable> tModel = toModel(target);

            Quantity<IMeasurable> quantity = new Quantity<>(sModel.getValue(), sModel.getUnit());
            Quantity<IMeasurable> converted = quantity.convertTo(tModel.getUnit());

            // Need to find the correct DTO unit to return
            QuantityDTO.IMeasurableUnit dtoUnit = target.getUnit();
            QuantityDTO result = new QuantityDTO(converted.getValue(), dtoUnit);
            
            repository.save(new QuantityMeasurementEntity("CONVERT", source.toString(), result.toString()));
            return result;
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("CONVERT", String.valueOf(source), null, e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit) {
        try {
            validateNotNull(q1, "Operand 1");
            validateNotNull(q2, "Operand 2");
            validateNotNull(targetUnit, "Target Unit");
            validateSameCategory(q1, q2, "ADD");
            validateSameCategory(q1, targetUnit, "ADD");

            QuantityModel<IMeasurable> m1 = toModel(q1);
            QuantityModel<IMeasurable> m2 = toModel(q2);
            QuantityModel<IMeasurable> mT = toModel(targetUnit);

            Quantity<IMeasurable> quantity1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> quantity2 = new Quantity<>(m2.getValue(), m2.getUnit());

            Quantity<IMeasurable> sum = quantity1.add(quantity2, mT.getUnit());
            QuantityDTO result = new QuantityDTO(sum.getValue(), targetUnit.getUnit());
            
            repository.save(new QuantityMeasurementEntity("ADD", q1.toString(), q2.toString(), result.toString()));
            return result;
        } catch (UnsupportedOperationException e) {
            repository.save(new QuantityMeasurementEntity("ADD", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("ADD", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit) {
        try {
            validateNotNull(q1, "Operand 1");
            validateNotNull(q2, "Operand 2");
            validateNotNull(targetUnit, "Target Unit");
            validateSameCategory(q1, q2, "SUBTRACT");
            validateSameCategory(q1, targetUnit, "SUBTRACT");

            QuantityModel<IMeasurable> m1 = toModel(q1);
            QuantityModel<IMeasurable> m2 = toModel(q2);
            QuantityModel<IMeasurable> mT = toModel(targetUnit);

            Quantity<IMeasurable> quantity1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> quantity2 = new Quantity<>(m2.getValue(), m2.getUnit());

            Quantity<IMeasurable> diff = quantity1.subtract(quantity2, mT.getUnit());
            QuantityDTO result = new QuantityDTO(diff.getValue(), targetUnit.getUnit());
            
            repository.save(new QuantityMeasurementEntity("SUBTRACT", q1.toString(), q2.toString(), result.toString()));
            return result;
        } catch (UnsupportedOperationException e) {
            repository.save(new QuantityMeasurementEntity("SUBTRACT", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("SUBTRACT", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        try {
            validateNotNull(q1, "Operand 1");
            validateNotNull(q2, "Operand 2");
            validateSameCategory(q1, q2, "DIVIDE");

            QuantityModel<IMeasurable> m1 = toModel(q1);
            QuantityModel<IMeasurable> m2 = toModel(q2);

            Quantity<IMeasurable> quantity1 = new Quantity<>(m1.getValue(), m1.getUnit());
            Quantity<IMeasurable> quantity2 = new Quantity<>(m2.getValue(), m2.getUnit());

            double ratio = quantity1.divide(quantity2);
            QuantityDTO result = new QuantityDTO(ratio, QuantityDTO.ResultUnit.SCALAR);
            
            repository.save(new QuantityMeasurementEntity("DIVIDE", q1.toString(), q2.toString(), String.valueOf(ratio)));
            return result;
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("DIVIDE", String.valueOf(q1), String.valueOf(q2), e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }
}
