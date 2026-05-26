package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.User;
import com.grouptrip.group_trip_backend.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "이메일을 입력해주세요.", null);
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "비밀번호를 입력해주세요.", null);
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return new AuthResponse(false, "이름을 입력해주세요.", null);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "이미 가입된 이메일입니다.", null);
        }

        User user = new User();
        user.setEmail(request.getEmail().trim());
        user.setPassword(request.getPassword().trim());
        user.setName(request.getName().trim());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new AuthResponse(true, "회원가입이 완료되었습니다.", savedUser);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "이메일을 입력해주세요.", null);
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "비밀번호를 입력해주세요.", null);
        }

        User user = userRepository.findByEmail(request.getEmail().trim());

        if (user == null) {
            return new AuthResponse(false, "가입되지 않은 이메일입니다.", null);
        }

        if (!user.getPassword().equals(request.getPassword().trim())) {
            return new AuthResponse(false, "비밀번호가 일치하지 않습니다.", null);
        }

        return new AuthResponse(true, "로그인 성공", user);
    }

    @Getter
    @Setter
    static class SignupRequest {
        private String email;
        private String password;
        private String name;
    }

    @Getter
    @Setter
    static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    static class AuthResponse {
        private boolean success;
        private String message;
        private User user;

        public AuthResponse(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
    }
}