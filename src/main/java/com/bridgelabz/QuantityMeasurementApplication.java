package com.bridgelabz;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UC17: Spring Boot Application Entry Point
 *
 * Bootstraps the Spring Boot application with embedded Tomcat server.
 * Replaces the manual main() method from UC16's QuantityMeasurementApp.java.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Quantity Measurement API",
                version = "1.0",
                description = "RESTful API for quantity measurement operations — UC17 Spring Boot Integration"
        )
)
public class QuantityMeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
    }
}
