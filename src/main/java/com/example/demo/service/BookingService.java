package com.example.demo.service;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.enums.BookingStatus;
import org.springframework.data.domain.*;

public interface BookingService {
    BookingResponse createBooking(String username, BookingRequest request);
    BookingResponse getById(Integer id);
    Page<BookingResponse> getMyBookings(String username, Pageable pageable);
    Page<BookingResponse> getAllBookings(BookingStatus status, Pageable pageable);
    BookingResponse confirmBooking(Integer id);
    BookingResponse cancelBooking(Integer id, String username);
    BookingResponse checkIn(Integer id);
    BookingResponse checkOut(Integer id);
    void expireBookings(); // Called by scheduler
}