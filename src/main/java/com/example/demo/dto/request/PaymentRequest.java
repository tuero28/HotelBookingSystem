package com.example.demo.dto.request;

import com.example.demo.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class PaymentRequest {
    @NotNull private Integer bookingId;
    @NotNull private PaymentMethod paymentMethod;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    private String transactionId;
}