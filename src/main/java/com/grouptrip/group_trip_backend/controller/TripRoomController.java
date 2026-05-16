package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.TripRoom;
import com.grouptrip.group_trip_backend.repository.DestinationCandidateRepository;
import com.grouptrip.group_trip_backend.repository.ExpenseRepository;
import com.grouptrip.group_trip_backend.repository.ScheduleRepository;
import com.grouptrip.group_trip_backend.repository.TripMemberRepository;
import com.grouptrip.group_trip_backend.repository.TripRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TripRoomController {

    private final TripRoomRepository tripRoomRepository;
    private final TripMemberRepository tripMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final DestinationCandidateRepository destinationCandidateRepository;
    private final ExpenseRepository expenseRepository;

    @PostMapping
    public TripRoom createTripRoom(@RequestBody TripRoom tripRoom) {
        return tripRoomRepository.save(tripRoom);
    }

    @GetMapping
    public List<TripRoom> getTripRooms() {
        return tripRoomRepository.findAll();
    }

    @GetMapping("/{id}")
    public TripRoom getTripRoom(@PathVariable Long id) {
        return tripRoomRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public TripRoom updateTripRoom(@PathVariable Long id, @RequestBody TripRoom updatedTripRoom) {
        TripRoom tripRoom = tripRoomRepository.findById(id).orElse(null);

        if (tripRoom == null) {
            return null;
        }

        tripRoom.setTitle(updatedTripRoom.getTitle());
        tripRoom.setDescription(updatedTripRoom.getDescription());
        tripRoom.setOwnerId(updatedTripRoom.getOwnerId());
        tripRoom.setStartDate(updatedTripRoom.getStartDate());
        tripRoom.setEndDate(updatedTripRoom.getEndDate());
        tripRoom.setDestination(updatedTripRoom.getDestination());

        if (updatedTripRoom.getStatus() != null) {
            tripRoom.setStatus(updatedTripRoom.getStatus());
        }

        return tripRoomRepository.save(tripRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteTripRoom(@PathVariable Long id) {
        tripMemberRepository.deleteAll(tripMemberRepository.findByTripRoomId(id));
        scheduleRepository.deleteAll(scheduleRepository.findByTripRoomId(id));
        destinationCandidateRepository.deleteAll(destinationCandidateRepository.findByTripRoomId(id));
        expenseRepository.deleteAll(expenseRepository.findByTripRoomId(id));

        tripRoomRepository.deleteById(id);
    }
}