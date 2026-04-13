package com.example.demo.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class RoomTypeResponse {
    private Integer roomTypeId;
    private String typeName;
    private String description;
    private BigDecimal basePrice;
    private Integer capacity;
    private List<String> amenities;
    private List<String> imageUrls;
    private String primaryImageUrl;
}