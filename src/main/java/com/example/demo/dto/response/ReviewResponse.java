package com.example.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ReviewResponse {
    private Integer reviewId;
    private Integer bookingId;
    private String customerName;
    private String roomTypeName;
    private Integer rating;
    private String comment;
    private String reply;
    private LocalDateTime reviewDate;
}