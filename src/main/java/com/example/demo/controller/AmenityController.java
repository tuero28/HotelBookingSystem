package com.example.demo.controller;

import com.example.demo.dto.request.AmenityRequest;
import com.example.demo.dto.response.AmenityResponse;
import com.example.demo.entity.Amenity;
import com.example.demo.repository.AmenityRepository;
import com.example.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {

    private final AmenityRepository amenityRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AmenityResponse>>> getAll() {
        List<AmenityResponse> responses = amenityRepository.findAll().stream()
                .map(a -> new AmenityResponse(a.getAmenityId(), a.getAmenityName(), a.getIconUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AmenityResponse>> create(@Valid @RequestBody AmenityRequest request) {
        Amenity amenity = Amenity.builder()
                .amenityName(request.getAmenityName())
                .iconUrl(request.getIconUrl())
                .build();
        amenity = amenityRepository.save(amenity);
        return ResponseEntity.status(201).body(ApiResponse.success(
                new AmenityResponse(amenity.getAmenityId(), amenity.getAmenityName(), amenity.getIconUrl())
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        if (!amenityRepository.existsById(id)) {
            throw new RuntimeException("Amenity not found");
        }
        amenityRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.noContent("Amenity deleted"));
    }
}
