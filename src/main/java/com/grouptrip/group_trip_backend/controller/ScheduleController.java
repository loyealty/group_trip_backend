package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.Schedule;
import com.grouptrip.group_trip_backend.repository.ScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @GetMapping("/trip-room/{tripRoomId}")
    public List<Schedule> getSchedulesByTripRoomId(@PathVariable Long tripRoomId) {
        return scheduleRepository.findByTripRoomId(tripRoomId);
    }
}