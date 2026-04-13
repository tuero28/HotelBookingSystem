package com.example.demo.service;

import com.example.demo.dto.request.RoomTypeRequest;
import com.example.demo.dto.response.RoomTypeResponse;
import java.util.List;

public interface RoomTypeService {
    List<RoomTypeResponse> getAll();
    RoomTypeResponse getById(Integer id);
    RoomTypeResponse create(RoomTypeRequest request);
    RoomTypeResponse update(Integer id, RoomTypeRequest request);
    void delete(Integer id);
}