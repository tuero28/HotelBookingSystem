package com.example.demo.service.impl;

import com.example.demo.dto.request.ReviewReplyRequest;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.BookingStatus;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.repository.*;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(String username, Integer bookingId, ReviewRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> AppException.notFound("Booking", bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED)
            throw AppException.badRequest("Can only review completed bookings");

        if (!booking.getCustomer().getUsername().equals(username))
            throw AppException.forbidden("You can only review your own bookings");

        if (reviewRepository.existsByBooking_BookingId(bookingId))
            throw AppException.conflict("Review already exists for this booking");

        Review review = Review.builder()
                .booking(booking)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponse replyToReview(Integer reviewId, ReviewReplyRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> AppException.notFound("Review", reviewId));
        review.setReply(request.getReply());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public Page<ReviewResponse> getByRoomType(Integer roomTypeId, Pageable pageable) {
        return reviewRepository.findByBooking_Room_RoomType_RoomTypeId(roomTypeId, pageable)
                .map(reviewMapper::toResponse);
    }

    @Override
    public Double getAverageRating(Integer roomTypeId) {
        return reviewRepository.averageRatingByRoomType(roomTypeId);
    }

    @Override
    public java.util.List<ReviewResponse> getAll() {
        return reviewRepository.findAll(
                org.springframework.data.domain.Sort.by("reviewDate").descending()
        ).stream().map(reviewMapper::toResponse).collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw AppException.notFound("Review", reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}