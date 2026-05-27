package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.User;
import com.grouptrip.group_trip_backend.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

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
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        String name = request.getName() == null ? "" : request.getName().trim();

        if (name.isEmpty()) {
            return new AuthResponse(false, "이름을 입력해주세요.", null);
        }

        if (email.isEmpty()) {
            return new AuthResponse(false, "이메일을 입력해주세요.", null);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new AuthResponse(false, "올바른 이메일 형식으로 입력해주세요.", null);
        }

        if (password.isEmpty()) {
            return new AuthResponse(false, "비밀번호를 입력해주세요.", null);
        }

        if (password.length() < 4) {
            return new AuthResponse(false, "비밀번호는 4자 이상 입력해주세요.", null);
        }

        if (userRepository.existsByEmail(email)) {
            return new AuthResponse(false, "이미 가입된 이메일입니다.", null);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new AuthResponse(true, "회원가입이 완료되었습니다.", savedUser);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (email.isEmpty()) {
            return new AuthResponse(false, "이메일을 입력해주세요.", null);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new AuthResponse(false, "올바른 이메일 형식으로 입력해주세요.", null);
        }

        if (password.isEmpty()) {
            return new AuthResponse(false, "비밀번호를 입력해주세요.", null);
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new AuthResponse(false, "가입되지 않은 이메일입니다.", null);
        }

        if (!user.getPassword().equals(password)) {
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