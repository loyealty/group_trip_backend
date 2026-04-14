package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTripRoomId(Long tripRoomId);
}