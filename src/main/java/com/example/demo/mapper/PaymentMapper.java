package com.example.demo.mapper;

import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse toResponse(Payment p) {
        if (p == null) return null;
        return PaymentResponse.builder()
                .paymentId(p.getPaymentId())
                .bookingId(p.getBooking() != null ? p.getBooking().getBookingId() : null)
                .paymentMethod(p.getPaymentMethod())
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .paymentStatus(p.getPaymentStatus())
                .transactionId(p.getTransactionId())
                .build();
    }
}