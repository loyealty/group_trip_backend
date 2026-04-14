package com.grouptrip.group_trip_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destination_candidates")
@Getter
@Setter
@NoArgsConstructor
public class DestinationCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tripRoomId;

    private String name;

    private String region;

    private String description;

    private Integer votes;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.votes == null) {
            this.votes = 0;
        }
    }
}