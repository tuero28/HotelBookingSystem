package com.example.demo.dto.response;

import com.example.demo.enums.RoomStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class RoomResponse {
    @JsonProperty("roomID")
    private Integer roomId;
    
    private String roomNumber;
    private RoomStatus status;
    private String roomTypeName;
    
    @JsonProperty("roomTypeID")
    private Integer roomTypeId;
    
    private BigDecimal basePrice;
    private Integer capacity;
}