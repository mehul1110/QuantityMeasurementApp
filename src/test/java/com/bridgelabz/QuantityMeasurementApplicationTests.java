package com.bridgelabz;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.dto.QuantityInputDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;
import com.bridgelabz.exception.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UC17: Spring Boot Integration Tests.
 *
 * @SpringBootTest(webEnvironment = RANDOM_PORT):
 *   - Starts the FULL Spring application context on a random port.
 *   - Uses a real embedded H2 database.
 *   - Tests the complete stack: Controller → Service → Repository → DB.
 *
 * TestRestTemplate:
 *   - Performs actual HTTP requests against the running application.
 *   - Validates end-to-end behaviour including serialisation/deserialisation.
 *
 * These tests cover:
 * - Application context loads successfully.
 * - All REST endpoint operations (compare, convert, add, subtract, divide).
 * - History and count retrieval.
 * - Error handling for invalid inputs.
 * - H2 database persistence verification.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1/quantities";
    }

    private QuantityInputDTO buildInput(String thisUnit, String thisMType, double thisVal,
                                        String thatUnit, String thatMType, double thatVal) {
        return new QuantityInputDTO(
                new QuantityDTO(thisVal, thisUnit, thisMType),
                new QuantityDTO(thatVal, thatUnit, thatMType)
        );
    }

    private HttpEntity<QuantityInputDTO> jsonEntity(QuantityInputDTO input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(input, headers);
    }

    // =========================================================
    // Test: Application context loads
    // =========================================================

    @Test
    @DisplayName("Spring Boot application context loads without errors")
    void contextLoads() {
        assertNotNull(restTemplate);
    }

    // =========================================================
    // Test: POST /compare
    // =========================================================

    @Test
    @DisplayName("POST /compare - 1 FEET == 12 INCHES → true")
    void testRestEndpointCompareQuantities() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("compare", response.getBody().getOperation());
        assertEquals("true", response.getBody().getResultString());
        assertFalse(response.getBody().isError());
    }

    @Test
    @DisplayName("POST /compare - 1 FEET != 2 FEET → false")
    void testRestEndpointCompareQuantities_NotEqual() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "FEET", "LengthUnit", 2.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("false", response.getBody().getResultString());
    }

    // =========================================================
    // Test: POST /convert
    // =========================================================

    @Test
    @DisplayName("POST /convert - 1 FEET → INCHES = 12.0")
    void testRestEndpointConvertQuantities() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 0.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/convert",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("convert", response.getBody().getOperation());
        assertEquals(12.0, response.getBody().getResultValue(), 0.001);
    }

    @Test
    @DisplayName("POST /convert - 100 CELSIUS → FAHRENHEIT = 212.0")
    void testRestEndpointConvertTemperature() {
        QuantityInputDTO input = buildInput("CELSIUS", "TemperatureUnit", 100.0,
                                            "FAHRENHEIT", "TemperatureUnit", 0.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/convert",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(212.0, response.getBody().getResultValue(), 0.01);
    }

    // =========================================================
    // Test: POST /add
    // =========================================================

    @Test
    @DisplayName("POST /add - 1 FEET + 12 INCHES = 2 FEET")
    void testRestEndpointAddQuantities() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("add", response.getBody().getOperation());
        assertEquals(2.0, response.getBody().getResultValue(), 0.001);
        assertEquals("FEET", response.getBody().getResultUnit());
    }

    @Test
    @DisplayName("POST /add - 1 KILOGRAM + 1000 GRAM = 2 KILOGRAM")
    void testRestEndpointAddWeights() {
        QuantityInputDTO input = buildInput("KILOGRAM", "WeightUnit", 1.0,
                                            "GRAM", "WeightUnit", 1000.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2.0, response.getBody().getResultValue(), 0.001);
    }

    // =========================================================
    // Test: POST /subtract
    // =========================================================

    @Test
    @DisplayName("POST /subtract - 2 FEET - 12 INCHES = 1 FEET")
    void testRestEndpointSubtractQuantities() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 2.0,
                                            "INCHES", "LengthUnit", 12.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/subtract",
                jsonEntity(input),
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1.0, response.getBody().getResultValue(), 0.001);
    }

    // =========================================================
    // Test: POST /divide
    // =========================================================

    @Test
    @DisplayName("POST /divide - divide by zero → 400 (service wraps ArithmeticException in domain exception)")
    void testRestEndpointDivideByZero_Returns500() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 0.0);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl() + "/divide",
                jsonEntity(input),
                ErrorResponse.class);

        // ArithmeticException thrown by Quantity.divide() is caught by service,
        // wrapped in QuantityMeasurementException → GlobalExceptionHandler returns 400.
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================
    // Test: Error cases
    // =========================================================

    @Test
    @DisplayName("POST /add - LengthUnit + WeightUnit → 400 Bad Request")
    void testRestEndpointIncompatibleTypes_Returns400() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "KILOGRAM", "WeightUnit", 1.0);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl() + "/add",
                jsonEntity(input),
                ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Quantity Measurement Error", response.getBody().getError());
    }

    @Test
    @DisplayName("POST /compare - invalid unit INCHE → 400 Bad Request")
    void testRestEndpointInvalidUnit_Returns400() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHE", "LengthUnit", 12.0);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                jsonEntity(input),
                ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================
    // Test: History endpoints
    // =========================================================

    @Test
    @DisplayName("GET /history/operation/compare → returns list of measurements")
    void testRestEndpointGetOperationHistory() {
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/operation/compare",
                QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("GET /history/type/LengthUnit → returns list")
    void testRestEndpointGetMeasurementHistory() {
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/type/LengthUnit",
                QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /count/compare → returns non-negative count")
    void testRestEndpointGetOperationCount() {
        ResponseEntity<Long> response = restTemplate.getForEntity(
                baseUrl() + "/count/compare",
                Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() >= 0);
    }

    @Test
    @DisplayName("GET /history/errored → returns list of errored records")
    void testRestEndpointGetErrorHistory() {
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/errored",
                QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =========================================================
    // Test: Swagger UI & Actuator
    // =========================================================

    @Test
    @DisplayName("GET /swagger-ui/index.html → Swagger UI accessible (200)")
    void testSwaggerUILoads() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/swagger-ui/index.html",
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /api-docs → OpenAPI JSON accessible (200)")
    void testOpenAPIDocumentation() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api-docs",
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /actuator/health → status UP (200)")
    void testActuatorHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health",
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }

    @Test
    @DisplayName("GET /h2-console → H2 console accessible (200)")
    void testH2ConsoleLaunches() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/h2-console",
                String.class);
        // H2 console redirects to login page
        assertTrue(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection());
    }

    // =========================================================
    // Test: Content Negotiation
    // =========================================================

    @Test
    @DisplayName("POST /compare - Content-Type: application/json works correctly")
    void testContentNegotiation_JSON() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<QuantityInputDTO> entity = new HttpEntity<>(input, headers);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                entity,
                QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(MediaType.APPLICATION_JSON.isCompatibleWith(
                response.getHeaders().getContentType()));
    }

    // =========================================================
    // Test: HTTP Status Codes
    // =========================================================

    @Test
    @DisplayName("Successful operations return 200 OK")
    void testHttpStatusCodes_Success() {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare", jsonEntity(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Client errors (invalid input) return 400 Bad Request")
    void testHttpStatusCodes_ClientErrors() {
        QuantityInputDTO input = buildInput("FOOT", "LengthUnit", 1.0,  // FOOT is invalid
                                            "INCHE", "LengthUnit", 12.0); // INCHE is invalid

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl() + "/compare", jsonEntity(input), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================
    // Test: Integration with JPA
    // =========================================================

    @Test
    @DisplayName("End-to-end: perform operation, then verify it appears in history")
    void testIntegrationTest_MultipleOperations() {
        // Perform a compare operation
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);
        restTemplate.postForEntity(baseUrl() + "/compare", jsonEntity(input), QuantityMeasurementDTO.class);

        // Verify it appears in history
        ResponseEntity<QuantityMeasurementDTO[]> historyResponse = restTemplate.getForEntity(
                baseUrl() + "/history/operation/compare", QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, historyResponse.getStatusCode());
        assertNotNull(historyResponse.getBody());
        assertTrue(historyResponse.getBody().length > 0);
    }
}
