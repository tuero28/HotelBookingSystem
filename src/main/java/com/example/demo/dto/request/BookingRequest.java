package com.example.demo.dto.request;

import com.example.demo.enums.BookingType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
public class BookingRequest {
    @NotNull private Integer roomId;
    @NotNull @FutureOrPresent private LocalDate checkInDate;
    @NotNull @Future private LocalDate checkOutDate;
    @Min(1) private Integer guestCount = 1;
    private String specialRequest;
    private BookingType bookingType = BookingType.ONLINE;
}