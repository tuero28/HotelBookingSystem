package com.example.demo.dto.response;

import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.BookingType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class BookingResponse {
    private Integer bookingId;
    private String customerName;
    private String roomNumber;
    private String roomTypeName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestCount;
    private String specialRequest;
    private BigDecimal totalPrice;
    private BigDecimal depositAmount;
    private BookingStatus status;
    private BookingType bookingType;
    private LocalDateTime expirationTime;
    private LocalDateTime createdAt;
    
    private Integer roomId;
    private Boolean hasReview;
    private ReviewResponse review;
}