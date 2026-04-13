package com.example.demo.converter;

import com.example.demo.enums.BookingType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingTypeConverter implements AttributeConverter<BookingType, String> {
    @Override public String convertToDatabaseColumn(BookingType a) { return a == null ? null : a.getDbValue(); }
    @Override public BookingType convertToEntityAttribute(String d) { return d == null ? null : BookingType.fromDbValue(d); }
}