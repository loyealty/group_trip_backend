package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.DestinationCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationCandidateRepository extends JpaRepository<DestinationCandidate, Long> {
    List<DestinationCandidate> findByTripRoomId(Long tripRoomId);
}