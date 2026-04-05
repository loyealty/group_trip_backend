package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.TripRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRoomRepository extends JpaRepository<TripRoom, Long> {
}