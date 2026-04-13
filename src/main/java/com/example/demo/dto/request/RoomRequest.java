package com.example.demo.dto.request;

import com.example.demo.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
public class RoomRequest {
    @NotBlank private String roomNumber;
    @NotNull private Integer roomTypeId;
    private RoomStatus status;
}