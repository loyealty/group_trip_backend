package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.TripMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripMemberRepository extends JpaRepository<TripMember, Long> {

    List<TripMember> findByTripRoomId(Long tripRoomId);
}