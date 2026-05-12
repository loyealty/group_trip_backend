package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.TripMember;
import com.grouptrip.group_trip_backend.repository.TripMemberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-members")
@CrossOrigin(origins = "*")
public class TripMemberController {

    private final TripMemberRepository tripMemberRepository;

    public TripMemberController(TripMemberRepository tripMemberRepository) {
        this.tripMemberRepository = tripMemberRepository;
    }

    @PostMapping
    public TripMember createTripMember(@RequestBody TripMember tripMember) {
        return tripMemberRepository.save(tripMember);
    }

    @GetMapping
    public List<TripMember> getTripMembers() {
        return tripMemberRepository.findAll();
    }

    @GetMapping("/trip-room/{tripRoomId}")
    public List<TripMember> getTripMembersByTripRoomId(@PathVariable Long tripRoomId) {
        return tripMemberRepository.findByTripRoomId(tripRoomId);
    }
}