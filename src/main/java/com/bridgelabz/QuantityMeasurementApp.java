package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.repository.QuantityMeasurementDatabaseRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementApp {
    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    public static void main(String[] args) {
        log.info("Starting Quantity Measurement Application...");

        // 1. Initialize Configuration and Repository Factory
        ApplicationConfig config = ApplicationConfig.getInstance();
        
        IQuantityMeasurementRepository repository = config.useDatabaseRepository()
                ? new QuantityMeasurementDatabaseRepository()
                : QuantityMeasurementCacheRepository.getInstance();

        log.info("Using Repository: {}", repository.getClass().getSimpleName());

        // 2. Initialize Layers (Dependency Injection)
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        // 3. Demonstration Scenarios (UC15 flow preserved)
        
        log.info("Scenario 1: Comparing length units");
        controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        log.info("Scenario 2: Converting length units");
        controller.performConversion(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCH)
        );

        log.info("Scenario 3: Adding length units");
        controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD),
                new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.YARD)
        );

        log.info("Scenario 4: Comparing temperature units");
        controller.performComparison(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(212.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );

        log.info("Scenario 5: Adding temperature units (Expected to fail)");
        controller.performAddition(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS)
        );

        log.info("Scenario 6: Cross-category comparison (Expected to fail)");
        controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
        );

        log.info("Application flow completed.");
        
        // Shutdown pool if using database
        if (repository instanceof QuantityMeasurementDatabaseRepository dbRepo) {
            dbRepo.close();
        }
    }
}