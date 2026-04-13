package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "Users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID") private Integer userId;

    @Column(name = "Username", nullable = false, unique = true, length = 50) private String username;
    @Column(name = "Password", nullable = false, length = 255) private String password;
    @Column(name = "FullName", length = 100) private String fullName;
    @Column(name = "Email", unique = true, length = 100) private String email;
    @Column(name = "Phone", unique = true, length = 20) private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleID") private Role role;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false) private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Booking> bookings;
}