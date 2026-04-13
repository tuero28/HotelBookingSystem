package com.example.demo.dto.response;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AmenityResponse {
    private Integer amenityId;
    private String name;
    private String description;
}
