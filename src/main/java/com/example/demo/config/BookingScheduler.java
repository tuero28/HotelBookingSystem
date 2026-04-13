package com.example.demo.config;

import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class BookingScheduler {

    private final BookingService bookingService;

    /** Mỗi phút kiểm tra booking Online quá hạn và tự động hủy */
    @Scheduled(fixedRate = 60_000)
    public void expireBookings() {
        log.info("Running booking expiration check...");
        bookingService.expireBookings();
    }
}