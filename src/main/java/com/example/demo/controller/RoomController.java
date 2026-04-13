package com.example.demo.controller;

import com.example.demo.dto.request.RoomRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.enums.RoomStatus;
import com.example.demo.service.RoomService;
import com.example.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(roomService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getById(id)));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> findAvailable(
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer guestCount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return ResponseEntity.ok(ApiResponse.success(roomService.findAvailable(roomTypeId, guestCount, checkIn, checkOut)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> create(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(roomService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> update(@PathVariable Integer id,
                                                            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(ApiResponse.success(roomService.update(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<RoomResponse>> updateStatus(@PathVariable Integer id,
                                                                  @RequestParam RoomStatus status) {
        return ResponseEntity.ok(ApiResponse.success(roomService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        roomService.delete(id);
        return ResponseEntity.ok(ApiResponse.noContent("Room deleted"));
    }
}