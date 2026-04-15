package com.bridgelabz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // Fields are intentionally NOT final.
    // Java Object Serialization does not support final fields.
    // Immutability is enforced through constructor-only initialization
    // and no setter methods being provided.
    private String operationType;
    private String operand1;
    private String operand2;
    private String result;
    private boolean hasError;
    private String errorMessage;
    private LocalDateTime timestamp;

    // Single operand constructor (CONVERT)
    public QuantityMeasurementEntity(String operationType, String operand1, String result) {
        this.operationType = operationType;
        this.operand1 = operand1;
        this.result = result;
        this.hasError = false;
        this.timestamp = LocalDateTime.now();
    }

    // Binary operand constructor (ADD, COMPARE, SUBTRACT, DIVIDE)
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2, String result) {
        this.operationType = operationType;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
        this.hasError = false;
        this.timestamp = LocalDateTime.now();
    }

    // Error constructor
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2, String errorMessage, boolean hasError) {
        this.operationType = operationType;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.errorMessage = errorMessage;
        this.hasError = hasError;
        this.timestamp = LocalDateTime.now();
    }

    public String getOperationType() { return operationType; }
    public String getOperand1() { return operand1; }
    public String getOperand2() { return operand2; }
    public String getResult() { return result; }
    public boolean isHasError() { return hasError; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = timestamp.format(formatter);
        if (hasError) {
            return String.format("[%s] ERROR: %s | Op: %s | Operands: %s, %s", 
                    timeStr, errorMessage, operationType, operand1, operand2);
        }
        return String.format("[%s] SUCCESS: %s | Op: %s | Operands: %s%s", 
                timeStr, result, operationType, operand1, (operand2 != null ? ", " + operand2 : ""));
    }
}
