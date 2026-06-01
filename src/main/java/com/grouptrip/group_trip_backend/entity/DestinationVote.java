package com.grouptrip.group_trip_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "destination_votes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"trip_room_id", "user_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DestinationVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_room_id")
    private Long tripRoomId;

    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(name = "user_id")
    private Long userId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}