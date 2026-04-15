package com.bridgelabz.service;

import com.bridgelabz.dto.QuantityDTO;

public interface IQuantityMeasurementService {
    QuantityDTO compare(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO convert(QuantityDTO source, QuantityDTO target);
    QuantityDTO add(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit);
    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
}
