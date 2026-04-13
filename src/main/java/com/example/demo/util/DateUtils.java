package com.example.demo.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    private DateUtils() {}

    /** Số đêm giữa checkIn và checkOut */
    public static long countNights(LocalDate checkIn, LocalDate checkOut) {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /** Kiểm tra 2 khoảng thời gian có giao nhau không */
    public static boolean isOverlapping(LocalDate start1, LocalDate end1,
                                        LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}