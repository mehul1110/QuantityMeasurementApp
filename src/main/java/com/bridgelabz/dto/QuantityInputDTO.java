package com.bridgelabz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * UC17: Input DTO for REST API requests.
 *
 * Wraps two QuantityDTO objects as the request body for all operations.
 */
public class QuantityInputDTO {

    @NotNull(message = "First quantity (thisQuantityDTO) cannot be null")
    @Valid
    private QuantityDTO thisQuantityDTO;

    @NotNull(message = "Second quantity (thatQuantityDTO) cannot be null")
    @Valid
    private QuantityDTO thatQuantityDTO;

    public QuantityInputDTO() {}

    public QuantityInputDTO(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        this.thisQuantityDTO = thisQuantityDTO;
        this.thatQuantityDTO = thatQuantityDTO;
    }

    public QuantityDTO getThisQuantityDTO() { return thisQuantityDTO; }
    public void setThisQuantityDTO(QuantityDTO thisQuantityDTO) { this.thisQuantityDTO = thisQuantityDTO; }

    public QuantityDTO getThatQuantityDTO() { return thatQuantityDTO; }
    public void setThatQuantityDTO(QuantityDTO thatQuantityDTO) { this.thatQuantityDTO = thatQuantityDTO; }
}
