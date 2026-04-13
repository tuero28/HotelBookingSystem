package com.example.demo.controller;

import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.DashboardStatsResponse;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class AdminController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalRooms = roomRepository.count();
        long totalBookings = bookingRepository.count();
        
        BigDecimal totalRevenue = bookingRepository.findAll().stream()
                .map(b -> b.getTotalPrice() != null ? b.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BookingResponse> recentBookings = bookingRepository.findAll(
                org.springframework.data.domain.PageRequest.of(0, 5, org.springframework.data.domain.Sort.by("createdAt").descending()))
                .stream().map(bookingMapper::toResponse).toList();
        
        Map<String, Long> roomStatusSummary = roomRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getStatus().name(), Collectors.counting()));

        DashboardStatsResponse stats = DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalRooms(totalRooms)
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .recentBookings(recentBookings)
                .roomStatusSummary(roomStatusSummary)
                .build();
                
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
