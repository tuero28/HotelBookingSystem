package com.example.demo.service.impl;

import com.example.demo.dto.request.RoomRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.RoomStatus;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.repository.*;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Override public List<RoomResponse> getAll() {
        return roomRepository.findAll().stream().map(roomMapper::toResponse).collect(Collectors.toList());
    }

    @Override public RoomResponse getById(Integer id) {
        return roomMapper.toResponse(findById(id));
    }

    @Override @Transactional
    public RoomResponse create(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber()))
            throw AppException.conflict("Room number already exists: " + request.getRoomNumber());
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> AppException.notFound("RoomType", request.getRoomTypeId()));
        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .roomType(roomType)
                .status(request.getStatus() != null ? request.getStatus() : RoomStatus.Available)
                .build();
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override @Transactional
    public RoomResponse update(Integer id, RoomRequest request) {
        Room room = findById(id);
        if (!room.getRoomNumber().equals(request.getRoomNumber())
                && roomRepository.existsByRoomNumber(request.getRoomNumber()))
            throw AppException.conflict("Room number already exists");
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> AppException.notFound("RoomType", request.getRoomTypeId()));
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(roomType);
        if (request.getStatus() != null) room.setStatus(request.getStatus());
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override @Transactional
    public void delete(Integer id) {
        if (!roomRepository.existsById(id)) throw AppException.notFound("Room", id);
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomResponse> findAvailable(Integer roomTypeId, Integer guestCount, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(roomTypeId, guestCount, checkIn, checkOut)
                .stream().map(roomMapper::toResponse).collect(Collectors.toList());
    }

    @Override @Transactional
    public RoomResponse updateStatus(Integer id, RoomStatus status) {
        Room room = findById(id);
        room.setStatus(status);
        return roomMapper.toResponse(roomRepository.save(room));
    }

    private Room findById(Integer id) {
        return roomRepository.findById(id).orElseThrow(() -> AppException.notFound("Room", id));
    }
}