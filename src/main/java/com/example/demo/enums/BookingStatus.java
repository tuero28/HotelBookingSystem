package com.example.demo.enums;

public enum BookingStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    IN_PROGRESS("In-progress"),
    COMPLETED("Completed");

    private final String dbValue;

    BookingStatus(String dbValue) { this.dbValue = dbValue; }

    @com.fasterxml.jackson.annotation.JsonValue
    public String getDbValue() { return dbValue; }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static BookingStatus fromDbValue(String value) {
        for (BookingStatus s : values())
            if (s.dbValue.equalsIgnoreCase(value)) return s;
        throw new IllegalArgumentException("Unknown BookingStatus: " + value);
    }
}