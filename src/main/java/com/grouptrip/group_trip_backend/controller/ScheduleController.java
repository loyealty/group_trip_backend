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

    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));

        schedule.setTitle(request.getTitle());
        schedule.setLocation(request.getLocation());
        schedule.setDescription(request.getDescription());
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setScheduleTime(request.getScheduleTime());

        return scheduleRepository.save(schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));

        scheduleRepository.delete(schedule);
    }
}