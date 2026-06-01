package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.DestinationCandidate;
import com.grouptrip.group_trip_backend.entity.DestinationVote;
import com.grouptrip.group_trip_backend.repository.DestinationCandidateRepository;
import com.grouptrip.group_trip_backend.repository.DestinationVoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destination-candidates")
@CrossOrigin(origins = "*")
public class DestinationCandidateController {

    private final DestinationCandidateRepository destinationCandidateRepository;
    private final DestinationVoteRepository destinationVoteRepository;

    public DestinationCandidateController(
            DestinationCandidateRepository destinationCandidateRepository,
            DestinationVoteRepository destinationVoteRepository
    ) {
        this.destinationCandidateRepository = destinationCandidateRepository;
        this.destinationVoteRepository = destinationVoteRepository;
    }

    @PostMapping
    public DestinationCandidate createDestinationCandidate(@RequestBody DestinationCandidate candidate) {
        if (candidate.getVotes() == null) {
            candidate.setVotes(0);
        }

        if (candidate.getConfirmed() == null) {
            candidate.setConfirmed(false);
        }

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
    public DestinationCandidate voteDestinationCandidate(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        DestinationCandidate candidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        boolean alreadyVoted = destinationVoteRepository.existsByTripRoomIdAndUserId(
                candidate.getTripRoomId(),
                userId
        );

        if (alreadyVoted) {
            throw new RuntimeException("이미 투표했습니다.");
        }

        DestinationVote vote = new DestinationVote();
        vote.setTripRoomId(candidate.getTripRoomId());
        vote.setCandidateId(candidate.getId());
        vote.setUserId(userId);
        destinationVoteRepository.save(vote);

        int currentVotes = candidate.getVotes() == null ? 0 : candidate.getVotes();
        candidate.setVotes(currentVotes + 1);

        return destinationCandidateRepository.save(candidate);
    }

    @PutMapping("/{id}/confirm")
    public DestinationCandidate confirmDestinationCandidate(@PathVariable Long id) {
        DestinationCandidate selectedCandidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        List<DestinationCandidate> candidates =
                destinationCandidateRepository.findByTripRoomId(selectedCandidate.getTripRoomId());

        for (DestinationCandidate candidate : candidates) {
            candidate.setConfirmed(false);
        }

        selectedCandidate.setConfirmed(true);

        destinationCandidateRepository.saveAll(candidates);

        return destinationCandidateRepository.save(selectedCandidate);
    }

    @Transactional
    @PutMapping("/{id}/cancel-confirm")
    public DestinationCandidate cancelConfirmDestinationCandidate(@PathVariable Long id) {
        DestinationCandidate selectedCandidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        Long tripRoomId = selectedCandidate.getTripRoomId();

        List<DestinationCandidate> candidates =
                destinationCandidateRepository.findByTripRoomId(tripRoomId);

        for (DestinationCandidate candidate : candidates) {
            candidate.setConfirmed(false);
            candidate.setVotes(0);
        }

        destinationVoteRepository.deleteByTripRoomId(tripRoomId);
        destinationCandidateRepository.saveAll(candidates);

        selectedCandidate.setConfirmed(false);
        selectedCandidate.setVotes(0);

        return selectedCandidate;
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

        if (request.getTripRoomId() != null) {
            candidate.setTripRoomId(request.getTripRoomId());
        }

        if (request.getVotes() != null) {
            candidate.setVotes(request.getVotes());
        }

        if (request.getConfirmed() != null) {
            candidate.setConfirmed(request.getConfirmed());
        }

        return destinationCandidateRepository.save(candidate);
    }

    @DeleteMapping("/{id}")
    public void deleteDestinationCandidate(@PathVariable Long id) {
        DestinationCandidate candidate = destinationCandidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지 후보를 찾을 수 없습니다."));

        destinationCandidateRepository.delete(candidate);
    }
}