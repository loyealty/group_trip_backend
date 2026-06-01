package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.DestinationVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationVoteRepository extends JpaRepository<DestinationVote, Long> {
    boolean existsByTripRoomIdAndUserId(Long tripRoomId, Long userId);

    void deleteByTripRoomId(Long tripRoomId);
}