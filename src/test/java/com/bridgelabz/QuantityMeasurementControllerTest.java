package com.bridgelabz;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.dto.QuantityInputDTO;
import com.bridgelabz.dto.QuantityMeasurementDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UC17: REST Controller Unit Tests using Spring MockMvc.
 *
 * @WebMvcTest(QuantityMeasurementController.class):
 *   - Creates a minimal Spring context with only the web layer.
 *   - Does NOT start the full application context or connect to a real database.
 *
 * @MockBean:
 *   - Creates a Mockito mock of IQuantityMeasurementService.
 *   - Injected into the Spring context so the controller uses it.
 *   - Service behavior defined with Mockito.when() for each test.
 *
 * @Autowired MockMvc:
 *   - Performs HTTP requests and assertions without starting a real server.
 *
 * @Autowired ObjectMapper:
 *   - Serialises request body objects to JSON for test requests.
 */
@WebMvcTest(controllers = com.bridgelabz.controller.QuantityMeasurementController.class)
@WithMockUser
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================
    // Helper: Build a sample QuantityMeasurementDTO result
    // =========================================================
    private QuantityMeasurementDTO buildCompareResult(boolean equal) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("FEET");
        dto.setThisMeasurementType("LengthUnit");
        dto.setThatValue(12.0);
        dto.setThatUnit("INCHES");
        dto.setThatMeasurementType("LengthUnit");
        dto.setOperation("compare");
        dto.setResultString(String.valueOf(equal));
        dto.setResultValue(0.0);
        dto.setError(false);
        return dto;
    }

    private QuantityInputDTO buildInput(String thisUnit, String thisMType, double thisVal,
                                        String thatUnit, String thatMType, double thatVal) {
        QuantityDTO thisDto = new QuantityDTO(thisVal, thisUnit, thisMType);
        QuantityDTO thatDto = new QuantityDTO(thatVal, thatUnit, thatMType);
        return new QuantityInputDTO(thisDto, thatDto);
    }

    // =========================================================
    // Tests: POST /compare
    // =========================================================

    @Test
    @DisplayName("POST /compare - 1 FEET == 12 INCHES → true")
    void testCompareQuantities_Equal() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);
        QuantityMeasurementDTO expected = buildCompareResult(true);

        when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    @DisplayName("POST /compare - invalid unit INCHE → 400 Bad Request")
    void testCompareQuantities_InvalidUnit_Returns400() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHE", "LengthUnit", 12.0);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // Tests: POST /convert
    // =========================================================

    @Test
    @DisplayName("POST /convert - 1 FEET → INCHES = 12.0")
    void testConvertQuantity() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 0.0);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("convert");
        expected.setResultValue(12.0);
        expected.setResultUnit("INCHES");
        expected.setResultMeasurementType("LengthUnit");
        expected.setError(false);

        when(service.convert(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/convert")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(12.0))
                .andExpect(jsonPath("$.resultUnit").value("INCHES"));
    }

    // =========================================================
    // Tests: POST /add
    // =========================================================

    @Test
    @DisplayName("POST /add - 1 FEET + 12 INCHES = 2 FEET")
    void testAddQuantities() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("add");
        expected.setResultValue(2.0);
        expected.setResultUnit("FEET");
        expected.setResultMeasurementType("LengthUnit");
        expected.setError(false);

        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    @Test
    @DisplayName("POST /add - incompatible types (LengthUnit + WeightUnit) → 400")
    void testAddQuantities_IncompatibleTypes() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "KILOGRAM", "WeightUnit", 1.0);

        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenThrow(new QuantityMeasurementException(
                        "add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));

        mockMvc.perform(post("/api/v1/quantities/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    // =========================================================
    // Tests: POST /subtract
    // =========================================================

    @Test
    @DisplayName("POST /subtract - 2 FEET - 12 INCHES = 1 FEET")
    void testSubtractQuantities() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 2.0,
                                            "INCHES", "LengthUnit", 12.0);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("subtract");
        expected.setResultValue(1.0);
        expected.setResultUnit("FEET");
        expected.setError(false);

        when(service.subtract(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/subtract")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(1.0));
    }

    // =========================================================
    // Tests: POST /divide
    // =========================================================

    @Test
    @DisplayName("POST /divide - divide by zero → 500 Internal Server Error")
    void testDivideByZero_Returns500() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 0.0);

        // Service wraps ArithmeticException in QuantityMeasurementException (domain error → 400)
        // but raw ArithmeticException from service mock goes to generic handler → 500
        when(service.divide(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenThrow(new ArithmeticException("Divide by zero"));

        mockMvc.perform(post("/api/v1/quantities/divide")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError());
    }

    // =========================================================
    // Tests: GET /history/operation/{operation}
    // =========================================================

    @Test
    @DisplayName("GET /history/operation/COMPARE → returns list of measurements")
    void testGetOperationHistory() throws Exception {
        QuantityMeasurementDTO item = buildCompareResult(true);
        when(service.getHistoryByOperation("COMPARE"))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"));
    }

    @Test
    @DisplayName("GET /history/operation/ADD → empty list when no data")
    void testGetOperationHistory_Empty() throws Exception {
        when(service.getHistoryByOperation("ADD"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // =========================================================
    // Tests: GET /history/type/{type}
    // =========================================================

    @Test
    @DisplayName("GET /history/type/LengthUnit → returns measurements for LengthUnit")
    void testGetMeasurementHistory() throws Exception {
        QuantityMeasurementDTO item = buildCompareResult(true);
        when(service.getHistoryByMeasurementType("LengthUnit"))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].thisMeasurementType").value("LengthUnit"));
    }

    // =========================================================
    // Tests: GET /count/{operation}
    // =========================================================

    @Test
    @DisplayName("GET /count/COMPARE → returns count of successful comparisons")
    void testGetOperationCount() throws Exception {
        when(service.getCountByOperation("COMPARE")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    // =========================================================
    // Tests: GET /history/errored
    // =========================================================

    @Test
    @DisplayName("GET /history/errored → returns errored measurements")
    void testGetErrorHistory() throws Exception {
        QuantityMeasurementDTO errored = new QuantityMeasurementDTO();
        errored.setOperation("add");
        errored.setError(true);
        errored.setErrorMessage("Cannot perform arithmetic between different measurement categories");

        when(service.getErrorHistory()).thenReturn(List.of(errored));

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].error").value(true))
                .andExpect(jsonPath("$[0].errorMessage").exists());
    }

    // =========================================================
    // Tests: Validation
    // =========================================================

    @Test
    @DisplayName("POST /compare - missing required field → 400 Bad Request")
    void testMissingRequiredField_Returns400() throws Exception {
        // Missing thatQuantityDTO
        String invalidJson = """
                {
                    "thisQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LengthUnit" }
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /compare - invalid measurementType pattern → 400 Bad Request")
    void testInvalidMeasurementType_Returns400() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "InvalidType", 1.0,
                                            "INCHES", "LengthUnit", 12.0);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /compare - null value → 400 Bad Request")
    void testNullValue_Returns400() throws Exception {
        String jsonWithNullValue = """
                {
                    "thisQuantityDTO": { "value": null, "unit": "FEET", "measurementType": "LengthUnit" },
                    "thatQuantityDTO": { "value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit" }
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithNullValue))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /add - Mockito.verify confirms service was called")
    void testMockitoVerify_ServiceCalled() throws Exception {
        QuantityInputDTO input = buildInput("FEET", "LengthUnit", 1.0,
                                            "INCHES", "LengthUnit", 12.0);
        QuantityMeasurementDTO mockResult = new QuantityMeasurementDTO();
        mockResult.setOperation("add");
        mockResult.setResultValue(2.0);

        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(mockResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)));

        Mockito.verify(service, Mockito.times(1))
                .add(any(QuantityDTO.class), any(QuantityDTO.class));
    }
}
