package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByBooking_BookingId(Integer bookingId);
    boolean existsByBooking_BookingId(Integer bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.booking.room.roomType.roomTypeId = :roomTypeId")
    Double averageRatingByRoomType(@Param("roomTypeId") Integer roomTypeId);

    Page<Review> findByBooking_Room_RoomType_RoomTypeId(Integer roomTypeId, Pageable pageable);
}