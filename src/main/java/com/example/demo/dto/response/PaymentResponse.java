package com.example.demo.dto.response;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private Integer paymentId;
    private Integer bookingId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
    private String transactionId;
}
