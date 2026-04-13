package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "Reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID") private Integer reviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingID", unique = true) private Booking booking;

    @Column(name = "Rating") private Integer rating;
    @Column(name = "Comment", columnDefinition = "TEXT") private String comment;
    @Column(name = "Reply", columnDefinition = "TEXT") private String reply;

    @CreationTimestamp
    @Column(name = "ReviewDate", updatable = false) private LocalDateTime reviewDate;
}