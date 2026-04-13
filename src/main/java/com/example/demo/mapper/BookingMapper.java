package com.example.demo.mapper;

import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponse toResponse(Booking b) {
        if (b == null) return null;
        return BookingResponse.builder()
                .bookingId(b.getBookingId())
                .customerName(b.getCustomer() != null ? b.getCustomer().getFullName() : null)
                .roomNumber(b.getRoom() != null ? b.getRoom().getRoomNumber() : null)
                .roomTypeName(b.getRoom() != null && b.getRoom().getRoomType() != null
                        ? b.getRoom().getRoomType().getTypeName() : null)
                .checkInDate(b.getCheckInDate())
                .checkOutDate(b.getCheckOutDate())
                .guestCount(b.getGuestCount())
                .specialRequest(b.getSpecialRequest())
                .totalPrice(b.getTotalPrice())
                .depositAmount(b.getDepositAmount())
                .status(b.getStatus())
                .bookingType(b.getBookingType())
                .expirationTime(b.getExpirationTime())
                .roomId(b.getRoom() != null ? b.getRoom().getRoomId() : null)
                .hasReview(b.getReview() != null)
                .review(b.getReview() != null ? ReviewResponse.builder()
                        .reviewId(b.getReview().getReviewId())
                        .rating(b.getReview().getRating())
                        .comment(b.getReview().getComment())
                        .reply(b.getReview().getReply())
                        .reviewDate(b.getReview().getReviewDate())
                        .build() : null)
                .createdAt(b.getCreatedAt())
                .build();
    }
}