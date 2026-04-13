package com.example.demo.service.impl;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.BookingType;
import com.example.demo.enums.RoomStatus;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.*;
import com.example.demo.service.BookingService;
import com.example.demo.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    /** Thời gian giữ chỗ cho booking Online (phút) */
    private static final int EXPIRATION_MINUTES = 30;

    @Override
    @Transactional
    public BookingResponse createBooking(String username, BookingRequest request) {
        // Validate dates
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate()))
            throw AppException.badRequest("Check-out date must be after check-in date");

        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User", username));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> AppException.notFound("Room", request.getRoomId()));

        if (room.getStatus() != RoomStatus.Available)
            throw AppException.badRequest("Room is not available");

        // Kiểm tra booking trùng
        if (bookingRepository.existsOverlappingBooking(room.getRoomId(),
                request.getCheckInDate(), request.getCheckOutDate()))
            throw AppException.conflict("Room is already booked for the selected dates");

        // Kiểm tra sức chứa
        if (request.getGuestCount() > room.getRoomType().getCapacity())
            throw AppException.badRequest("Guest count exceeds room capacity of " + room.getRoomType().getCapacity());

        // Tính tổng tiền
        long nights = DateUtils.countNights(request.getCheckInDate(), request.getCheckOutDate());
        BigDecimal totalPrice = room.getRoomType().getBasePrice().multiply(BigDecimal.valueOf(nights));
        BigDecimal deposit = request.getBookingType() == BookingType.ONLINE
                ? totalPrice.multiply(BigDecimal.valueOf(0.3)) : BigDecimal.ZERO;

        LocalDateTime expiration = request.getBookingType() == BookingType.ONLINE
                ? LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES) : null;

        Booking booking = Booking.builder()
                .customer(customer)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .guestCount(request.getGuestCount())
                .specialRequest(request.getSpecialRequest())
                .totalPrice(totalPrice)
                .depositAmount(deposit)
                .status(BookingStatus.PENDING)
                .bookingType(request.getBookingType())
                .expirationTime(expiration)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getById(Integer id) {
        return bookingMapper.toResponse(findBookingById(id));
    }

    @Override
    public Page<BookingResponse> getMyBookings(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User", username));
        return bookingRepository.findByCustomer_UserId(user.getUserId(), pageable)
                .map(bookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getAllBookings(BookingStatus status, Pageable pageable) {
        if (status != null)
            return bookingRepository.findByStatus(status, pageable).map(bookingMapper::toResponse);
        return bookingRepository.findAll(pageable).map(bookingMapper::toResponse);
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(Integer id) {
        Booking booking = findBookingById(id);
        if (booking.getStatus() != BookingStatus.PENDING)
            throw AppException.badRequest("Only PENDING bookings can be confirmed");
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Integer id, String username) {
        Booking booking = findBookingById(id);
        // Chỉ chủ booking hoặc ADMIN/STAFF được hủy
        boolean isOwner = booking.getCustomer() != null &&
                booking.getCustomer().getUsername().equals(username);
        if (!isOwner)
            throw AppException.forbidden("You are not allowed to cancel this booking");
        if (booking.getStatus() == BookingStatus.IN_PROGRESS || booking.getStatus() == BookingStatus.COMPLETED)
            throw AppException.badRequest("Cannot cancel a booking that is already " + booking.getStatus());
        booking.setStatus(BookingStatus.CANCELLED);
        // Trả phòng về Available
        if (booking.getRoom() != null) {
            booking.getRoom().setStatus(RoomStatus.Available);
            roomRepository.save(booking.getRoom());
        }
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse checkIn(Integer id) {
        Booking booking = findBookingById(id);
        if (booking.getStatus() != BookingStatus.CONFIRMED)
            throw AppException.badRequest("Booking must be CONFIRMED before check-in");
        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking.setActualCheckIn(LocalDateTime.now());
        if (booking.getRoom() != null) {
            booking.getRoom().setStatus(RoomStatus.Occupied);
            roomRepository.save(booking.getRoom());
        }
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse checkOut(Integer id) {
        Booking booking = findBookingById(id);
        if (booking.getStatus() != BookingStatus.IN_PROGRESS)
            throw AppException.badRequest("Booking must be IN_PROGRESS before check-out");
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setActualCheckOut(LocalDateTime.now());
        if (booking.getRoom() != null) {
            booking.getRoom().setStatus(RoomStatus.Cleaning);
            roomRepository.save(booking.getRoom());
        }
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void expireBookings() {
        List<Booking> expired = bookingRepository.findExpiredPendingBookings(LocalDateTime.now());
        expired.forEach(b -> b.setStatus(BookingStatus.CANCELLED));
        bookingRepository.saveAll(expired);
    }

    private Booking findBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Booking", id));
    }
}