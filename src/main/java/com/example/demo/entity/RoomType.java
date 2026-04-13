package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity @Table(name = "RoomTypes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomTypeID") private Integer roomTypeId;

    @Column(name = "TypeName", nullable = false, length = 100) private String typeName;
    @Column(name = "Description", columnDefinition = "TEXT") private String description;
    @Column(name = "BasePrice", nullable = false, precision = 12, scale = 2) private BigDecimal basePrice;
    @Column(name = "Capacity") private Integer capacity = 2;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<Room> rooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<RoomImage> roomImages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "RoomTypeAmenities",
            joinColumns = @JoinColumn(name = "RoomTypeID"),
            inverseJoinColumns = @JoinColumn(name = "AmenityID"))
    private java.util.Set<Amenity> amenities;
}