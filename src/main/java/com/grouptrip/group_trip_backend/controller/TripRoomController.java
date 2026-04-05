package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.TripRoom;
import com.grouptrip.group_trip_backend.repository.TripRoomRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trip-rooms")
public class TripRoomController {

    private final TripRoomRepository tripRoomRepository;

    public TripRoomController(TripRoomRepository tripRoomRepository) {
        this.tripRoomRepository = tripRoomRepository;
    }

    @PostMapping
    public TripRoom createTripRoom(@RequestBody TripRoom tripRoom) {
        tripRoom.setCreatedAt(LocalDateTime.now());
        if (tripRoom.getStatus() == null || tripRoom.getStatus().isEmpty()) {
            tripRoom.setStatus("PLANNING");
        }
        return tripRoomRepository.save(tripRoom);
    }

    @GetMapping
    public List<TripRoom> getTripRooms() {
        return tripRoomRepository.findAll();
    }
}