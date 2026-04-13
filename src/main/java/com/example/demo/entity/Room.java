package com.example.demo.entity;

import com.example.demo.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Table(name = "Rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID") private Integer roomId;

    @Column(name = "RoomNumber", nullable = false, unique = true, length = 10) private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomTypeID") private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status") private RoomStatus status = RoomStatus.Available;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Booking> bookings;
}