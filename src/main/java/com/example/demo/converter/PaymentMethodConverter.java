package com.example.demo.converter;

import com.example.demo.enums.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {
    @Override public String convertToDatabaseColumn(PaymentMethod a) { return a == null ? null : a.getDbValue(); }
    @Override public PaymentMethod convertToEntityAttribute(String d) { return d == null ? null : PaymentMethod.fromDbValue(d); }
}