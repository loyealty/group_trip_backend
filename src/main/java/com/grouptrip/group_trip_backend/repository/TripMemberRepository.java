package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.TripMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripMemberRepository extends JpaRepository<TripMember, Long> {
}