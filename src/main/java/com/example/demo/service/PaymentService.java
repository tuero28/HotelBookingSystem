package com.example.demo.service;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    List<PaymentResponse> getByBookingId(Integer bookingId);
    PaymentResponse refund(Integer paymentId);
    List<PaymentResponse> getAll();
}