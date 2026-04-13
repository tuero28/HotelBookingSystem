package com.example.demo.service.impl;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.repository.*;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;

    @Override @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> AppException.notFound("Booking", request.getBookingId()));

        if (booking.getStatus() == BookingStatus.CANCELLED)
            throw AppException.badRequest("Cannot pay for a cancelled booking");

        Payment payment = Payment.builder()
                .booking(booking)
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.COMPLETED)
                .transactionId(request.getTransactionId())
                .build();

        // Tự động confirm booking sau khi thanh toán đặt cọc
        if (booking.getStatus() == BookingStatus.PENDING)
            booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentResponse> getByBookingId(Integer bookingId) {
        return paymentRepository.findByBooking_BookingId(bookingId)
                .stream().map(paymentMapper::toResponse).collect(Collectors.toList());
    }

    @Override @Transactional
    public PaymentResponse refund(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> AppException.notFound("Payment", paymentId));
        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED)
            throw AppException.badRequest("Only completed payments can be refunded");
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll(
                org.springframework.data.domain.Sort.by("paymentDate").descending()
        ).stream().map(paymentMapper::toResponse).collect(Collectors.toList());
    }
}