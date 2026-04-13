
package com.example.demo.converter;

import com.example.demo.enums.BookingStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {
    @Override public String convertToDatabaseColumn(BookingStatus a) { return a == null ? null : a.getDbValue(); }
    @Override public BookingStatus convertToEntityAttribute(String d) { return d == null ? null : BookingStatus.fromDbValue(d); }
}



