package com.example.demo.service;

import com.example.demo.dto.request.ReviewReplyRequest;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import org.springframework.data.domain.*;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String username, Integer bookingId, ReviewRequest request);
    ReviewResponse replyToReview(Integer reviewId, ReviewReplyRequest request);
    Page<ReviewResponse> getByRoomType(Integer roomTypeId, Pageable pageable);
    Double getAverageRating(Integer roomTypeId);
    List<ReviewResponse> getAll();
    void deleteReview(Integer reviewId);
}