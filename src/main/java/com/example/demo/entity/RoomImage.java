package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "RoomImages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ImageID") private Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomTypeID") private RoomType roomType;

    @Column(name = "ImageURL", nullable = false, length = 255) private String imageUrl;
    @Column(name = "isPrimary") private Boolean isPrimary = false;
}