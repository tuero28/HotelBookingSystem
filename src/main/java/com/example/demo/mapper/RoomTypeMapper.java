package com.example.demo.mapper;

import com.example.demo.dto.response.RoomTypeResponse;
import com.example.demo.entity.RoomImage;
import com.example.demo.entity.RoomType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomTypeMapper {
    public RoomTypeResponse toResponse(RoomType rt) {
        if (rt == null) return null;

        List<String> amenities = rt.getAmenities() == null ? Collections.emptyList()
                : rt.getAmenities().stream().map(a -> a.getAmenityName()).collect(Collectors.toList());

        List<String> images = rt.getRoomImages() == null ? Collections.emptyList()
                : rt.getRoomImages().stream().map(RoomImage::getImageUrl).collect(Collectors.toList());

        String primary = rt.getRoomImages() == null ? null
                : rt.getRoomImages().stream().filter(i -> Boolean.TRUE.equals(i.getIsPrimary()))
                .map(RoomImage::getImageUrl).findFirst().orElse(null);

        return RoomTypeResponse.builder()
                .roomTypeId(rt.getRoomTypeId())
                .typeName(rt.getTypeName())
                .description(rt.getDescription())
                .basePrice(rt.getBasePrice())
                .capacity(rt.getCapacity())
                .amenities(amenities)
                .imageUrls(images)
                .primaryImageUrl(primary)
                .build();
    }
}