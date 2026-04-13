package com.example.demo.entity;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "Payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID") private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingID") private Booking booking;

    @Convert(converter = com.example.demo.converter.PaymentMethodConverter.class)
    @Column(name = "PaymentMethod", columnDefinition = "ENUM('Cash','Credit Card','Bank Transfer','E-Wallet')") private PaymentMethod paymentMethod;

    @Column(name = "Amount", precision = 12, scale = 2) private BigDecimal amount;
    @Column(name = "PaymentDate") private LocalDateTime paymentDate;

    @Convert(converter = com.example.demo.converter.PaymentStatusConverter.class)
    @Column(name = "PaymentStatus", columnDefinition = "ENUM('Pending','Completed','Failed','Refunded')") private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "TransactionID", length = 100) private String transactionId;
}