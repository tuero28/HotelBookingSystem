package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AmenityRequest {
    @NotBlank private String amenityName;
    private String iconUrl;
}
