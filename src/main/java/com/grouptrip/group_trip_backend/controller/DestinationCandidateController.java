package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.DestinationCandidate;
import com.grouptrip.group_trip_backend.repository.DestinationCandidateRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destination-candidates")
@CrossOrigin(origins = "*")
public class DestinationCandidateController {

    private final DestinationCandidateRepository destinationCandidateRepository;

    public DestinationCandidateController(DestinationCandidateRepository destinationCandidateRepository) {
        this.destinationCandidateRepository = destinationCandidateRepository;
    }

    @PostMapping
    public DestinationCandidate createDestinationCandidate(@RequestBody DestinationCandidate candidate) {
        return destinationCandidateRepository.save(candidate);
    }

    @GetMapping
    public List<DestinationCandidate> getAllDestinationCandidates() {
        return destinationCandidateRepository.findAll();
    }

    @GetMapping("/trip-room/{tripRoomId}")
    public List<DestinationCandidate> getCandidatesByTripRoomId(@PathVariable Long tripRoomId) {
        return destinationCandidateRepository.findByTripRoomId(tripRoomId);
    }
}