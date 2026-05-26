package com.grouptrip.group_trip_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TripRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Long ownerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String destination;

    private String status;

    @Column(nullable = false, unique = true, length = 20)
    private String inviteCode;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "PLANNING";
        }
    }
}