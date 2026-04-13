package com.example.demo.repository;

import com.example.demo.entity.RoomType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    @Query("SELECT DISTINCT rt FROM RoomType rt LEFT JOIN FETCH rt.amenities LEFT JOIN FETCH rt.roomImages")
    List<RoomType> findAllWithDetails();

    @Query("SELECT rt FROM RoomType rt LEFT JOIN FETCH rt.amenities LEFT JOIN FETCH rt.roomImages WHERE rt.roomTypeId = :id")
    Optional<RoomType> findByIdWithDetails(@Param("id") Integer id);
}