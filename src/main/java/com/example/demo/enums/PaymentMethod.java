package com.example.demo.enums;

public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer"),
    E_WALLET("E-Wallet");

    private final String dbValue;

    PaymentMethod(String dbValue) { this.dbValue = dbValue; }

    @com.fasterxml.jackson.annotation.JsonValue
    public String getDbValue() { return dbValue; }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static PaymentMethod fromDbValue(String value) {
        for (PaymentMethod m : values())
            if (m.dbValue.equalsIgnoreCase(value)) return m;
        throw new IllegalArgumentException("Unknown PaymentMethod: " + value);
    }
}