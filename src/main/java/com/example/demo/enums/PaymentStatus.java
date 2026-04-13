package com.example.demo.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    REFUNDED("Refunded");

    private final String dbValue;

    PaymentStatus(String dbValue) { this.dbValue = dbValue; }

    @com.fasterxml.jackson.annotation.JsonValue
    public String getDbValue() { return dbValue; }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static PaymentStatus fromDbValue(String value) {
        for (PaymentStatus s : values())
            if (s.dbValue.equalsIgnoreCase(value)) return s;
        throw new IllegalArgumentException("Unknown PaymentStatus: " + value);
    }
}