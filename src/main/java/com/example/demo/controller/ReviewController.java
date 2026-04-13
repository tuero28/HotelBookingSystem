package com.example.demo.controller;

import com.example.demo.dto.request.ReviewReplyRequest;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
import com.example.demo.util.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> create(
            @AuthenticationPrincipal String username,
            @PathVariable Integer bookingId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(reviewService.createReview(username, bookingId, request)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<java.util.List<ReviewResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAll()));
    }

    @PatchMapping("/{reviewId}/reply")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<ReviewResponse>> reply(
            @PathVariable Integer reviewId,
            @Valid @RequestBody ReviewReplyRequest request) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.replyToReview(reviewId, request)));
    }

    @GetMapping("/room-type/{roomTypeId}")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getByRoomType(
            @PathVariable Integer roomTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("reviewDate").descending());
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.from(reviewService.getByRoomType(roomTypeId, pageable))));
    }

    @GetMapping("/room-type/{roomTypeId}/rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Integer roomTypeId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAverageRating(roomTypeId)));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}