package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class RoomTypeRequest {
    @NotBlank private String typeName;
    private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal basePrice;
    @Min(1) private Integer capacity;
}