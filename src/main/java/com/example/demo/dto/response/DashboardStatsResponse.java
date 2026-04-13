package com.example.demo.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalRooms;
    private long totalBookings;
    private BigDecimal totalRevenue;
    private List<BookingResponse> recentBookings;
    private Map<String, Long> roomStatusSummary;
}
