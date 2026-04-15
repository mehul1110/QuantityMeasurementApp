package com.bridgelabz.controller;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.service.IQuantityMeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementController {
    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementController.class);
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new QuantityMeasurementException("Service cannot be null");
        }
        this.service = service;
    }

    public void performComparison(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.compare(q1, q2);
            boolean isEqual = result.getValue() == 1.0;
            System.out.println("Input: " + q1 + " vs " + q2);
            System.out.println("Result: " + (isEqual ? "EQUAL" : "NOT EQUAL"));
        } catch (QuantityMeasurementException e) {
            log.warn("Comparison failed: {}", e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performConversion(QuantityDTO source, QuantityDTO target) {
        try {
            QuantityDTO result = service.convert(source, target);
            System.out.println("Input: Convert " + source + " to " + target.getUnitName());
            System.out.println("Result: " + result);
        } catch (QuantityMeasurementException e) {
            log.warn("Conversion failed: {}", e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performAddition(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit) {
        try {
            QuantityDTO result = service.add(q1, q2, targetUnit);
            System.out.println("Input: " + q1 + " + " + q2 + " in " + targetUnit.getUnitName());
            System.out.println("Result: " + result);
        } catch (QuantityMeasurementException e) {
            log.warn("Addition failed: {}", e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
}
