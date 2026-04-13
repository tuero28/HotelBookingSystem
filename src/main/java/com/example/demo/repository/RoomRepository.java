package com.example.demo.repository;

import com.example.demo.entity.Room;
import com.example.demo.enums.RoomStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByRoomNumber(String roomNumber);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByRoomType_RoomTypeId(Integer roomTypeId);

    /** Tìm phòng còn trống trong khoảng ngày cho trước */
    @Query("""
        SELECT r FROM Room r
        WHERE (:roomTypeId IS NULL OR r.roomType.roomTypeId = :roomTypeId)
          AND (:guestCount IS NULL OR r.roomType.capacity >= :guestCount)
          AND r.status = com.example.demo.enums.RoomStatus.Available
          AND r.roomId NOT IN (
              SELECT b.room.roomId FROM Booking b
              WHERE b.status <> com.example.demo.enums.BookingStatus.CANCELLED
                AND b.checkInDate < :checkOut
                AND b.checkOutDate > :checkIn
          )
        """)
    List<Room> findAvailableRooms(@Param("roomTypeId") Integer roomTypeId,
                                  @Param("guestCount") Integer guestCount,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);
}