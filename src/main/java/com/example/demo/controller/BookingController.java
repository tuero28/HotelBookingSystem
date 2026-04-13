package com.example.demo.controller;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.enums.BookingStatus;
import com.example.demo.service.BookingService;
import com.example.demo.util.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(bookingService.createBooking(username, request)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> myBookings(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(bookingService.getMyBookings(username, pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getAll(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(bookingService.getAllBookings(status, pageable))));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<BookingResponse>> confirm(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.confirmBooking(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancel(
            @PathVariable Integer id,
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelBooking(id, username)));
    }

    @PatchMapping("/{id}/check-in")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<BookingResponse>> checkIn(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.checkIn(id)));
    }

    @PatchMapping("/{id}/check-out")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<BookingResponse>> checkOut(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.checkOut(id)));
    }
}