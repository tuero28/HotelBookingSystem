package com.example.demo.controller;

import com.example.demo.dto.request.RoomTypeRequest;
import com.example.demo.dto.response.RoomTypeResponse;
import com.example.demo.service.RoomTypeService;
import com.example.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomTypeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(roomTypeService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(roomTypeService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> create(@Valid @RequestBody RoomTypeRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(roomTypeService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> update(@PathVariable Integer id,
                                                                @Valid @RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(roomTypeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        roomTypeService.delete(id);
        return ResponseEntity.ok(ApiResponse.noContent("RoomType deleted"));
    }
}