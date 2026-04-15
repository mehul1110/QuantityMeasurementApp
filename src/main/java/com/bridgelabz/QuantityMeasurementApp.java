package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement Application (N-Tier Architecture) ===");

        // 1. Initialize Layers (Dependency Injection)
        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        // 2. Demonstration Scenarios
        
        // 1. Compare 1.0 FEET vs 12.0 INCHES (LENGTH) -> should be true
        controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        // 2. Convert 1.0 FEET to INCHES (LENGTH) -> should be 12.0
        controller.performConversion(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCH)
        );

        // 3. Add 1.0 YARDS + 3.0 FEET in YARDS (LENGTH) -> should be 2.0
        controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD),
                new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.YARD)
        );

        // 4. Compare 100.0 CELSIUS vs 212.0 FAHRENHEIT (TEMPERATURE) -> should be true
        controller.performComparison(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(212.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );

        // 5. Add 100.0 CELSIUS + 50.0 CELSIUS (TEMPERATURE) -> error
        controller.performAddition(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS)
        );

        // 6. Compare 1.0 FEET (LENGTH) vs 1.0 KILOGRAM (WEIGHT) -> error
        controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
        );

        System.out.println("\n=== Demonstration Complete ===");
        System.out.println("Check 'quantity_measurements.ser' for persistent history.");
    }
}