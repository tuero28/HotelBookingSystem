package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.enums.BookingStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByCustomer_UserId(Integer customerId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    List<Booking> findByRoom_RoomId(Integer roomId);

    /** Tự động hủy booking Online quá hạn */
    @Query("""
        SELECT b FROM Booking b
        WHERE b.status = com.example.demo.enums.BookingStatus.PENDING
          AND b.bookingType = com.example.demo.enums.BookingType.ONLINE
          AND b.expirationTime < :now
        """)
    List<Booking> findExpiredPendingBookings(@Param("now") LocalDateTime now);

    /** Kiểm tra phòng có bị đặt trùng không */
    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.roomId = :roomId
          AND b.status <> com.example.demo.enums.BookingStatus.CANCELLED
          AND b.checkInDate < :checkOut
          AND b.checkOutDate > :checkIn
        """)
    boolean existsOverlappingBooking(@Param("roomId") Integer roomId,
                                     @Param("checkIn") java.time.LocalDate checkIn,
                                     @Param("checkOut") java.time.LocalDate checkOut);
}