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

    @PutMapping("/{id}/vote")
    public DestinationCandidate voteDestinationCandidate(@PathVariable Long id) {
        DestinationCandidate candidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        int currentVotes = candidate.getVotes() == null ? 0 : candidate.getVotes();
        candidate.setVotes(currentVotes + 1);

        return destinationCandidateRepository.save(candidate);
    }

    @PutMapping("/{id}")
    public DestinationCandidate updateDestinationCandidate(
            @PathVariable Long id,
            @RequestBody DestinationCandidate request
    ) {
        DestinationCandidate candidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        candidate.setName(request.getName());
        candidate.setRegion(request.getRegion());
        candidate.setDescription(request.getDescription());

        return destinationCandidateRepository.save(candidate);
    }

    @DeleteMapping("/{id}")
    public void deleteDestinationCandidate(@PathVariable Long id) {
        DestinationCandidate candidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        destinationCandidateRepository.delete(candidate);
    }
}