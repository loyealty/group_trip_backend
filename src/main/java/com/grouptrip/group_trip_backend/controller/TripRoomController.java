package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.TripMember;
import com.grouptrip.group_trip_backend.entity.TripRoom;
import com.grouptrip.group_trip_backend.entity.User;
import com.grouptrip.group_trip_backend.repository.DestinationCandidateRepository;
import com.grouptrip.group_trip_backend.repository.ExpenseRepository;
import com.grouptrip.group_trip_backend.repository.ScheduleRepository;
import com.grouptrip.group_trip_backend.repository.TripMemberRepository;
import com.grouptrip.group_trip_backend.repository.TripRoomRepository;
import com.grouptrip.group_trip_backend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private final UserRepository userRepository;

    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    @PostMapping
    public TripRoom createTripRoom(@RequestBody TripRoom tripRoom) {
        if (tripRoom.getInviteCode() == null || tripRoom.getInviteCode().isEmpty()) {
            tripRoom.setInviteCode(generateInviteCode());
        }

        TripRoom savedTripRoom = tripRoomRepository.save(tripRoom);

        if (savedTripRoom.getOwnerId() != null) {
            boolean alreadyMember = tripMemberRepository.existsByTripRoomIdAndUserId(
                    savedTripRoom.getId(),
                    savedTripRoom.getOwnerId()
            );

            if (!alreadyMember) {
                TripMember ownerMember = new TripMember();
                ownerMember.setTripRoomId(savedTripRoom.getId());
                ownerMember.setUserId(savedTripRoom.getOwnerId());
                ownerMember.setRole("OWNER");

                User owner = userRepository.findById(savedTripRoom.getOwnerId()).orElse(null);

                if (owner != null) {
                    ownerMember.setMemberName(owner.getName());
                } else {
                    ownerMember.setMemberName("방장");
                }

                tripMemberRepository.save(ownerMember);
            }
        }

        return savedTripRoom;
    }

    @GetMapping
    public List<TripRoom> getTripRooms() {
        return tripRoomRepository.findAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<TripRoom> getTripRoomsByOwnerId(@PathVariable Long ownerId) {
        return tripRoomRepository.findByOwnerId(ownerId);
    }

    @GetMapping("/user/{userId}")
    public List<TripRoom> getTripRoomsByUserId(@PathVariable Long userId) {
        List<TripMember> members = tripMemberRepository.findByUserId(userId);
        List<Long> joinedTripRoomIds = new ArrayList<>();

        for (TripMember member : members) {
            joinedTripRoomIds.add(member.getTripRoomId());
        }

        List<TripRoom> tripRooms = tripRoomRepository.findByOwnerIdOrIdIn(userId, joinedTripRoomIds);

        Map<Long, TripRoom> uniqueTripRooms = new LinkedHashMap<>();

        for (TripRoom tripRoom : tripRooms) {
            uniqueTripRooms.put(tripRoom.getId(), tripRoom);
        }

        return new ArrayList<>(uniqueTripRooms.values());
    }

    @GetMapping("/invite/{inviteCode}")
    public TripRoom getTripRoomByInviteCode(@PathVariable String inviteCode) {
        return tripRoomRepository.findByInviteCode(inviteCode);
    }

    @PostMapping("/join")
    public JoinTripRoomResponse joinTripRoom(@RequestBody JoinTripRoomRequest request) {
        if (request.getInviteCode() == null || request.getInviteCode().trim().isEmpty()) {
            return new JoinTripRoomResponse(false, "초대 코드를 입력해주세요.", null);
        }

        if (request.getUserId() == null) {
            return new JoinTripRoomResponse(false, "사용자 정보가 없습니다.", null);
        }

        TripRoom tripRoom = tripRoomRepository.findByInviteCode(
                request.getInviteCode().trim().toUpperCase()
        );

        if (tripRoom == null) {
            return new JoinTripRoomResponse(false, "존재하지 않는 초대 코드입니다.", null);
        }

        boolean alreadyJoined = tripMemberRepository.existsByTripRoomIdAndUserId(
                tripRoom.getId(),
                request.getUserId()
        );

        if (alreadyJoined) {
            return new JoinTripRoomResponse(false, "이미 참여 중인 여행방입니다.", tripRoom);
        }

        TripMember tripMember = new TripMember();
        tripMember.setTripRoomId(tripRoom.getId());
        tripMember.setUserId(request.getUserId());
        tripMember.setRole("MEMBER");

        if (request.getMemberName() != null && !request.getMemberName().trim().isEmpty()) {
            tripMember.setMemberName(request.getMemberName().trim());
        } else {
            User user = userRepository.findById(request.getUserId()).orElse(null);

            if (user != null) {
                tripMember.setMemberName(user.getName());
            } else {
                tripMember.setMemberName("참여자");
            }
        }

        tripMemberRepository.save(tripMember);

        return new JoinTripRoomResponse(true, "여행방에 참여했습니다.", tripRoom);
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

        if (updatedTripRoom.getInviteCode() != null && !updatedTripRoom.getInviteCode().isEmpty()) {
            tripRoom.setInviteCode(updatedTripRoom.getInviteCode());
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

    private String generateInviteCode() {
        String inviteCode;

        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                int index = random.nextInt(CODE_CHARS.length());
                builder.append(CODE_CHARS.charAt(index));
            }

            inviteCode = builder.toString();
        } while (tripRoomRepository.existsByInviteCode(inviteCode));

        return inviteCode;
    }

    @Getter
    @Setter
    static class JoinTripRoomRequest {
        private String inviteCode;
        private Long userId;
        private String memberName;
    }

    @Getter
    @Setter
    static class JoinTripRoomResponse {
        private boolean success;
        private String message;
        private TripRoom tripRoom;

        public JoinTripRoomResponse(boolean success, String message, TripRoom tripRoom) {
            this.success = success;
            this.message = message;
            this.tripRoom = tripRoom;
        }
    }
}