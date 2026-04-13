package com.example.demo.converter;

import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    @Override public String convertToDatabaseColumn(PaymentStatus a) { return a == null ? null : a.getDbValue(); }
    @Override public PaymentStatus convertToEntityAttribute(String d) { return d == null ? null : PaymentStatus.fromDbValue(d); }
}