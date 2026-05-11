package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.TripRoom;
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
}