package com.grouptrip.group_trip_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "trip_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_room_id", "member_name"})
)
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_room_id", nullable = false)
    private Long tripRoomId;

    @Column(name = "member_name", nullable = false, length = 50)
    private String memberName;

    @Column(length = 30)
    private String role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();

        if (this.role == null || this.role.isEmpty()) {
            this.role = "MEMBER";
        }
    }
}