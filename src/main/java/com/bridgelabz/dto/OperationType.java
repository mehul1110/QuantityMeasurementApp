package com.bridgelabz.dto;

/**
 * UC17: Enum representing all supported quantity measurement operation types.
 *
 * Used throughout the application to provide type-safe operation constants.
 * Ensures that only valid operations are used in the service layer,
 * repository queries, and API responses.
 */
public enum OperationType {

    /** Compare two quantities (returns true/false) */
    COMPARE,

    /** Convert a quantity from one unit to another */
    CONVERT,

    /** Add two quantities together */
    ADD,

    /** Subtract the second quantity from the first */
    SUBTRACT,

    /** Divide the first quantity by the second */
    DIVIDE;

    /**
     * Returns the operation type as a lowercase String for use in entity/DTO operation field.
     */
    public String toOperationString() {
        return this.name().toLowerCase();
    }
}
