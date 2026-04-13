package com.example.demo.service;

import com.example.demo.dto.request.RoomRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.enums.RoomStatus;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<RoomResponse> getAll();
    RoomResponse getById(Integer id);
    RoomResponse create(RoomRequest request);
    RoomResponse update(Integer id, RoomRequest request);
    void delete(Integer id);
    List<RoomResponse> findAvailable(Integer roomTypeId, Integer guestCount, LocalDate checkIn, LocalDate checkOut);
    RoomResponse updateStatus(Integer id, RoomStatus status);
}