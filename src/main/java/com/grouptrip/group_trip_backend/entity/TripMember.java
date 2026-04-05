package com.grouptrip.group_trip_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "trip_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_room_id", "user_id"})
)
public class TripMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_room_id", nullable = false)
    private Long tripRoomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 30)
    private String role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}