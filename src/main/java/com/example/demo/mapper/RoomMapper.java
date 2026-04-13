package com.example.demo.mapper;

import com.example.demo.dto.response.RoomResponse;
import com.example.demo.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomResponse toResponse(Room room) {
        if (room == null)
            return null;
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .status(room.getStatus())
                .roomTypeName(room.getRoomType() != null ? room.getRoomType().getTypeName() : null)
                .roomTypeId(room.getRoomType() != null ? room.getRoomType().getRoomTypeId() : null)
                .basePrice(room.getRoomType() != null ? room.getRoomType().getBasePrice() : null)
                .capacity(room.getRoomType() != null ? room.getRoomType().getCapacity() : null)
                .build();
    }
}