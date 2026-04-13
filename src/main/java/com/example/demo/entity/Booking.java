package com.example.demo.entity;

import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.BookingType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;

@Entity @Table(name = "Bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID") private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID") private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomID") private Room room;

    @Column(name = "CheckInDate", nullable = false) private LocalDate checkInDate;
    @Column(name = "CheckOutDate", nullable = false) private LocalDate checkOutDate;
    @Column(name = "GuestCount") private Integer guestCount = 1;
    @Column(name = "SpecialRequest", columnDefinition = "TEXT") private String specialRequest;
    @Column(name = "ActualCheckIn") private LocalDateTime actualCheckIn;
    @Column(name = "ActualCheckOut") private LocalDateTime actualCheckOut;
    @Column(name = "TotalPrice", precision = 12, scale = 2) private BigDecimal totalPrice;
    @Column(name = "DepositAmount", precision = 12, scale = 2) private BigDecimal depositAmount = BigDecimal.ZERO;

    // Dùng Converter thay vì @Enumerated để map "In-progress"
    @Convert(converter = com.example.demo.converter.BookingStatusConverter.class)
    @Column(name = "Status", columnDefinition = "ENUM('Pending','Confirmed','Cancelled','In-progress','Completed')") private BookingStatus status = BookingStatus.PENDING;

    @Convert(converter = com.example.demo.converter.BookingTypeConverter.class)
    @Column(name = "BookingType", columnDefinition = "ENUM('Online','PayAtHotel')") private BookingType bookingType = BookingType.ONLINE;

    @Column(name = "ExpirationTime") private LocalDateTime expirationTime;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false) private LocalDateTime createdAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;
}