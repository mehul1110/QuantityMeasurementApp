package com.bridgelabz.controller;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.service.IQuantityMeasurementService;

public class QuantityMeasurementController {
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new QuantityMeasurementException("Service cannot be null");
        }
        this.service = service;
    }

    public void performComparison(QuantityDTO q1, QuantityDTO q2) {
        System.out.println("\n--- Performing Comparison ---");
        System.out.println("Input: " + q1 + " vs " + q2);
        try {
            QuantityDTO result = service.compare(q1, q2);
            boolean isEqual = result.getValue() == 1.0;
            System.out.println("Result: " + (isEqual ? "EQUAL" : "NOT EQUAL"));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performConversion(QuantityDTO source, QuantityDTO targetUnit) {
        System.out.println("\n--- Performing Conversion ---");
        System.out.println("Input: Convert " + source + " to " + targetUnit.getUnit());
        try {
            QuantityDTO result = service.convert(source, targetUnit);
            System.out.println("Result: " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performAddition(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit) {
        System.out.println("\n--- Performing Addition ---");
        System.out.println("Input: " + q1 + " + " + q2 + " in " + targetUnit.getUnit());
        try {
            QuantityDTO result = service.add(q1, q2, targetUnit);
            System.out.println("Result: " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performSubtraction(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetUnit) {
        System.out.println("\n--- Performing Subtraction ---");
        System.out.println("Input: " + q1 + " - " + q2 + " in " + targetUnit.getUnit());
        try {
            QuantityDTO result = service.subtract(q1, q2, targetUnit);
            System.out.println("Result: " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performDivision(QuantityDTO q1, QuantityDTO q2) {
        System.out.println("\n--- Performing Division ---");
        System.out.println("Input: " + q1 + " / " + q2);
        try {
            QuantityDTO result = service.divide(q1, q2);
            System.out.println("Result: Scalar ratio = " + result.getValue());
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
