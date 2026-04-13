package com.example.demo.service.impl;

import com.example.demo.dto.request.RoomTypeRequest;
import com.example.demo.dto.response.RoomTypeResponse;
import com.example.demo.entity.RoomType;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.RoomTypeMapper;
import com.example.demo.repository.RoomTypeRepository;
import com.example.demo.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    @Override
    public List<RoomTypeResponse> getAll() {
        return roomTypeRepository.findAllWithDetails().stream()
                .map(roomTypeMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public RoomTypeResponse getById(Integer id) {
        return roomTypeMapper.toResponse(roomTypeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> AppException.notFound("RoomType", id)));
    }

    @Override
    @Transactional
    public RoomTypeResponse create(RoomTypeRequest request) {
        RoomType rt = RoomType.builder()
                .typeName(request.getTypeName())
                .description(request.getDescription())
                .basePrice(request.getBasePrice())
                .capacity(request.getCapacity() != null ? request.getCapacity() : 2)
                .build();
        return roomTypeMapper.toResponse(roomTypeRepository.save(rt));
    }

    @Override
    @Transactional
    public RoomTypeResponse update(Integer id, RoomTypeRequest request) {
        RoomType rt = roomTypeRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("RoomType", id));
        rt.setTypeName(request.getTypeName());
        rt.setDescription(request.getDescription());
        rt.setBasePrice(request.getBasePrice());
        if (request.getCapacity() != null) rt.setCapacity(request.getCapacity());
        return roomTypeMapper.toResponse(roomTypeRepository.save(rt));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!roomTypeRepository.existsById(id)) throw AppException.notFound("RoomType", id);
        roomTypeRepository.deleteById(id);
    }
}