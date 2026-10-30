package com.example.user_service.service.impl;

import com.example.user_service.dto.request.RegisterUserRequest;
import com.example.user_service.dto.response.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ApiException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterUserRequest request) {
        String email = request.getEmail();
        if (email == null || userRepository.findByEmail(email).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already registered or missing!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(request.getPassword());
        String role = request.getRole();
        user.setRole(role != null && !role.trim().isEmpty() ? role.trim().toLowerCase() : "user");
        user.setStatus("ACTIVE");
        user.setDeliveryPartner(null);
        return UserResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, null, "User not found."));
        return UserResponse.from(user);
    }

    @Override
    public Map<String, String> blockUser(Long id, String reason) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found."));
        user.setStatus("BLOCKED");
        user.setBlockReason(reason);
        userRepository.save(user);
        return Map.of("message", "User blocked successfully.");
    }

    @Override
    public Map<String, String> unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found."));
        user.setStatus("ACTIVE");
        user.setBlockReason("");
        userRepository.save(user);
        return Map.of("message", "User unblocked successfully.");
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .filter(candidate -> candidate.getPassword() != null && candidate.getPassword().equals(password))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "YOUR ACCOUNT IS BLOCKED BY ADMIN.");
            response.put("reason", user.getBlockReason() != null ? user.getBlockReason() : "Policy violation.");
            response.put("adminContact", "admin@gmail.com");
            throw new ApiException(HttpStatus.FORBIDDEN, response, "Account is blocked.");
        }
        return UserResponse.from(user);
    }

    @Override
    public Map<String, String> resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        Map.of("message", "User email ID not found in database."),
                        "User email ID not found in database."));
        user.setPassword(newPassword);
        userRepository.save(user);
        return Map.of("message", "Password updated successfully! You can now log in.");
    }
}
