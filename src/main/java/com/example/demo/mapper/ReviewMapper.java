package com.example.demo.mapper;

import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review r) {
        if (r == null) return null;
        String customer = (r.getBooking() != null && r.getBooking().getCustomer() != null)
                ? r.getBooking().getCustomer().getFullName() : null;
        String roomType = (r.getBooking() != null && r.getBooking().getRoom() != null
                && r.getBooking().getRoom().getRoomType() != null)
                ? r.getBooking().getRoom().getRoomType().getTypeName() : null;
        return ReviewResponse.builder()
                .reviewId(r.getReviewId())
                .bookingId(r.getBooking() != null ? r.getBooking().getBookingId() : null)
                .customerName(customer)
                .roomTypeName(roomType)
                .rating(r.getRating())
                .comment(r.getComment())
                .reply(r.getReply())
                .reviewDate(r.getReviewDate())
                .build();
    }
}