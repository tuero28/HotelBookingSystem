
package com.example.demo.entity;

import jakarta.persistence.*;
        import lombok.*;
        import java.util.List;

@Entity @Table(name = "Amenities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Amenity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AmenityID") private Integer amenityId;

    @Column(name = "AmenityName", nullable = false, length = 100) private String amenityName;
    @Column(name = "IconURL", length = 255) private String iconUrl;

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    private List<RoomType> roomTypes;
}