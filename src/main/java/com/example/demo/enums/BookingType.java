package com.example.demo.enums;

public enum BookingType {
    ONLINE("Online"),
    PAY_AT_HOTEL("PayAtHotel");

    private final String dbValue;

    BookingType(String dbValue) { this.dbValue = dbValue; }

    @com.fasterxml.jackson.annotation.JsonValue
    public String getDbValue() { return dbValue; }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static BookingType fromDbValue(String value) {
        for (BookingType t : values())
            if (t.dbValue.equalsIgnoreCase(value)) return t;
        throw new IllegalArgumentException("Unknown BookingType: " + value);
    }
}
